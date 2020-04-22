# -*- coding: utf-8 -*-
import sys
import json
import glob
import re
import os

class Var:
    def __init__(self, n):
        self.name = n
        self.values = []
        
    def __eq__(self, other):
        if  isinstance(other,Var):
            return self.name == other.name
        elif isinstance(other,str):
            return other == self.name
        else:
            return False

    def getName(self):
        return self.name

    def setType(self, t):
        self._type = t

    def getType(self):
        return self._type

    def isIn(self, v):
        return v in self.values

    def add(self, v):
        if self.isInteger():
            v = int(v)
        elif self.isDecimal():
            v = float(v)
        if v not in self.values:
            self.values.append(v)

    def getValues(self):
        return self.values

    def isNumeric(self):
        return self._type in ('short', 'int', 'long', 'float', 'double')

    def isInteger(self):
        return self._type in ('short', 'int', 'long')
    
    def isDecimal(self):
        return self._type in ('float', 'double')

def read_trace(location):
    traces = {}
    rootDir = os.getcwd()
    log = rootDir + "/" + location
    for files in os.listdir(log):
        if files.endswith('.dat'):
            f = open(log + files, "r")
            for line in f:
                l = re.sub(r"\s+","", line).split(':')
                if l[0] == 'Class':
                    if l[1] not in traces.keys():
                        traces[l[1]] = {}
                    curClass = traces[l[1]]
                elif l[0] == 'Method':
                    if l[1] not in curClass.keys():
                        curClass[l[1]] = []
                    curMethod = curClass[l[1]]
                elif l[0] == 'Variable':
                    if l[1] not in curMethod:
                        curMethod.append(Var(l[1]))
                        curVar = len(curMethod) - 1
                    else:
                        curVar = curMethod.index(l[1])
                elif l[0] == 'Type':
                    curMethod[curVar].setType(l[1])
                elif l[0] == 'Value':
                    curMethod[curVar].add(l[1])
    return traces

def verify_type(var):
    return not var.get_type() in ('byte', 'other')

def detect_type(var, value):
    if var.get_type() == 'char':
        return value
    if var.get_type() == 'boolean':
        return value == 'true'
    if var.get_type() in ('short', 'int', 'long'):
        return int(value)
    if var.get_type() in ('float', 'double'):
        return float(value)

    return value

def detect_invariant(traces):
    invariants = {}
    for className, c in traces.items():
        classMap = {}
        invariants[className] = classMap
        for methodName, m in c.items():
            methodList = []
            for v in m:
                
                maxValue = v.getValues()[0]
                minValue = v.getValues()[0]
                a = v.getValues()[0]

                # Constant Value: x = a
                p1 = v.getType() != 'other'
                # Uninitialized Value: x = uninit
                p2 = v.getType() == 'other'
                # Small Value Set: x ∈ {a,b,c}
                p3 = v.getType() != 'other'
                # Non-zero: x != 0
                p4 = v.isNumeric()
                valueSet = set()

                for value in v.getValues():
                    if p1:
                        p1 = value == a
                    if p2:
                        p2 = value == 'null'
                    if p3:
                        v.add(value)
                        p3 = len(valueSet) <= 3
                    if v.isNumeric():
                        maxValue = max(maxValue, value)
                        minValue = min(minValue, value)

                        if p4:
                            p4 = value != 0

                if p3:
                    p3 = len(valueSet) != 1
                if p1:
                    methodList.append('Constant Value: %s = %s' % (v.getName(), str(a)))
                if p2:
                    methodList.append('Uninitialized Value: %s = uninit' % (v.getName()))
                if p3:
                    methodList.append('Small Value Set: %s ∈ {%s}' % (v.getName(), ','.join(str(v) for v in valueSet)))
                if p4:
                    methodList.append('Non-zero: %s != 0' % (v.getName()))
                if v.isNumeric():
                    methodList.append('Range: %s ∈ [%s, %s]' % (v.getName(), str(minValue), str(maxValue)))

                if v.getType() == 'int' and not p1:
                    upper = max(maxValue, abs(minValue))
                    for b in (2, 3, 5, 7, 11, 13, 17, 19):
                        if b > upper:
                            break
                        p5 = True
                        p6 = True
                        a = v.getValues()[0] % b
                        for value in v.getValues():
                            if not p5 and not p6:
                                break
                            remainder = value % b
                            if p5:
                                p5 = remainder  == a
                            if p6:
                                p6 = remainder != 0
                        if p5:
                            methodList.append('Modulus: %s = %d (mod %d)' % (v.getName(), a, b))
                        if p6:
                            methodList.append('Non-modulus: %s != 0 (mod %d)' % (v.getName(), b))

                if methodList:
                    classMap[methodName] = methodList
    return invariants

def dump_invariants(invariants):
    with open('log', 'w+') as log:
        for className, class_map in invariants.items():
            print('CLASS: %s' % className)
            log.write('CLASS: %s\n' % className)
            for methodName, method_list in class_map.items():
                print('    METHOD: %s' % methodName)
                log.write('    METHOD: %s\n' % methodName)
                for inv in method_list:
                    print('        %s' % inv)
                    log.write('        %s\n' % inv)


if __name__ == "__main__":
    reload(sys)
    sys.setdefaultencoding('utf-8')
    if len(sys.argv) != 2:
        print "improper input!\ninput should be script then log directory"
    else:
        print "reading traces"
        traces = read_trace(sys.argv[1])
        print "detecting invariants"
        invariants = detect_invariant(traces)
        dump_invariants(invariants)
