************************************************************************
file with basedata            : mm50_.bas
initial value random generator: 40818033
************************************************************************
projects                      :  1
jobs (incl. supersource/sink ):  12
horizon                       :  85
RESOURCES
  - renewable                 :  2   R
  - nonrenewable              :  2   N
  - doubly constrained        :  0   D
************************************************************************
PROJECT INFORMATION:
pronr.  #jobs rel.date duedate tardcost  MPM-Time
    1     10      0       17        9       17
************************************************************************
PRECEDENCE RELATIONS:
jobnr.    #modes  #successors   successors
   1        1          3           2   3   4
   2        3          3           5   6   7
   3        3          2           5   8
   4        3          1           7
   5        3          1          10
   6        3          1           9
   7        3          2           8  11
   8        3          2           9  10
   9        3          1          12
  10        3          1          12
  11        3          1          12
  12        1          0        
************************************************************************
REQUESTS/DURATIONS:
jobnr. mode duration  R 1  R 2  N 1  N 2
------------------------------------------------------------------------
  1      1     0       0    0    0    0
  2      1     2       7   10    0    7
         2     5       6    7    4    0
         3     5       5    9    3    0
  3      1     6      10    5    0    8
         2     7      10    4    3    0
         3    10      10    4    0    8
  4      1     8      10   10    3    0
         2     9      10    8    0    8
         3    10      10    7    0    8
  5      1     3       5    8    7    0
         2     3       5    9    0    3
         3     7       2    7    5    0
  6      1     1       5    9    0    7
         2     3       5    6    0    4
         3     6       3    5    0    4
  7      1     5       9    8    0    8
         2     8       8    7    0    7
         3     9       7    6    0    4
  8      1     1       7    6    0    9
         2     8       7    6    7    0
         3    10       7    5    0    5
  9      1     2       5    9    7    0
         2     5       4    7    4    0
         3    10       2    7    4    0
 10      1     1       4    9    0    8
         2     4       4    7    2    0
         3    10       2    7    1    0
 11      1     4       9    7   10    0
         2     7       7    7    0    8
         3     8       4    7   10    0
 12      1     0       0    0    0    0
************************************************************************
RESOURCEAVAILABILITIES:
  R 1  R 2  N 1  N 2
   22   20   24   37
************************************************************************