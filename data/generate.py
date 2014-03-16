import os, collections

# 1. READ Ground truth into dict, with list of results
ground_path = "C:\\Users\\bong\\Dropbox\\Nus\\CS4242\\libsvm-3.17\\libsvm-3.17\\CS4242\\Groundtruth"
print (os.listdir(ground_path))
groundtruth = {}
for fn in os.listdir(ground_path):
    cur_fn = os.path.join(ground_path, fn)
    res = [line.rstrip('\n') for line in open(cur_fn)]
    for x in range(len(res)): # replace all 0 with -1
        if res[x] == '0':
            res[x] = '-1'
    groundtruth[fn] = res

# print(groundtruth)
# 'Groundtruth_DBS1.txt', DBS2, NUS1, NUS2, STARHUB

#################
# Helper function
def compute_scores(gt, pr):
    if not (len(gt) == len(pr)):
        print("Error! Lengths of gt and pre not equal!")
        exit()

    # precision
    tp = len([i for i, j in zip(gt, pr) if (i == j and i == '1')])
    tp_fp = collections.Counter(pr)['1']
    precision = tp/tp_fp if tp_fp else 0
    #print(precision)
    
    # Recall
    tp_fn = collections.Counter(gt)['1']
    recall = tp/tp_fn
    #print(recall)
    
    # F1
    f1 = 2 * (precision * recall) / (precision + recall) if (precision+recall != 0) else 0
    #print(f1)
    
    # input("Press Enter to continue...")

    return precision, recall, f1
#
#################

# 2. Parse results              
### base_path = "C:\\Users\\bong\\Dropbox\\Nus\\CS4242\\libsvm-3.17\\libsvm-3.17\\CS4242\\output-dataset-021714-T1251"  
base_path = "C:\\Users\\bong\\Dropbox\\Nus\\CS4242\\libsvm-3.17\\libsvm-3.17\\CS4242\\output-dataset-021714-T1612"
path_200 = "200\Output"
path_350 = "350\Output"
path_500 = "500\Output"

f = open('statistics.txt','w')
bigstore = []
output_paths = [path_200, path_350, path_500]
for xpath in output_paths:
    path = os.path.join(base_path, xpath)
    for fn in os.listdir(path):
        curr_fn = os.path.join(path, fn)
        predict = [line.rstrip('\n') for line in open(curr_fn)]

        lfn = fn.lower()

        if "dbs1" in lfn:
            pr, rc, f1 = compute_scores(groundtruth['Groundtruth_DBS1.txt'], predict)
        elif "dbs2" in lfn:
            pr, rc, f1 = compute_scores(groundtruth['Groundtruth_DBS2.txt'], predict)
        elif "nus1" in lfn:
            pr, rc, f1 = compute_scores(groundtruth['Groundtruth_NUS1.txt'], predict)
        elif "nus2" in lfn:
            pr, rc, f1 = compute_scores(groundtruth['Groundtruth_NUS2.txt'], predict)
        elif "starhub" in lfn:
            pr, rc, f1 = compute_scores(groundtruth['Groundtruth_STARHUB.txt'], predict)
        else:
            print("ERROR CHECK FILENAMES!")
            exit()

        result = "{} {} :: precision={}, recall={}, F1={} \n".format(xpath, fn, pr, rc, f1)
        f.write(result)

        result_formatted = "{}, {}, {:.4f}, {:.4f}, {:.4f}\n".format(xpath, fn, pr, rc, f1)
        bigstore.append(result_formatted)
        
f.close()

# Output grouped .csv for easier results table copying
f_formatted = open('statistics_formatted.csv','w')
argz = ['DBS1','DBS2','NUS1','NUS2','Starhub']
for a in argz:
    f_formatted.write("{},,,,\n".format(a))
    for line in bigstore:
        if a in line:
            # formatting shitz n gigglez
            line = line.replace('vector','')
            line = line.replace('(','')
            line = line.replace(')','')
            line = line.replace(a,'')
            line = line.replace('.OUTPUT','')
            
            f_formatted.write(line)
    f_formatted.write("\n\n\n")
f_formatted.close()
