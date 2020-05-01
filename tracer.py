import os
import sys

folder = sys.argv[1]
for fii in os.listdir(folder):
    file = open(os.path.join(folder, fii),'r')
    lines = file.readlines()
    #print(len(lines))

    tracker = -1
    range_start = -1
    vals = []

    for i in range(0,len(lines)-1):
        if(i==tracker):
            print(lines[i].strip())
            tracker = -1
        if(i>=range_start and range_start!=-1):
            if(lines[i].find(':')<0):
                vals.append(int(lines[i]))
            elif(lines[i].find(':')>0):
                range_start = -1
                vals.sort()
                if(len(vals)>1):
                    #print(vals[0])
                    #print(" ")
                    #print(vals[-1])
                    print('ranges from {} to {}'.format(vals[0],vals[-1]))
                    vals = []
                elif(len(vals)==1):
                    print(vals[0])
                    vals = []
        str = lines[i].split(':')
        if((lines[i].find(':'))>0):
            print(lines[i].strip())
            if(len(str)==2 and str[0]=='var_type' and str[1]==' boolean\n'):
                    tracker = i+2
            elif(len(str)==2 and str[0]=='var_type'): #if other var type
                range_start = i+2
                #print("other var type found")

