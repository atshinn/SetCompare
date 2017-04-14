The purpose of PresetCompartor is to isolate the differences between the presets in two .pst files and write them to a .txt file. The user has the option of choosing two specific .pst files
or they can choose two directories that contain the same number of .pst files. If two directories were chosen then the corresponding .pst files will be compared
(i.e. black.pst v black.pst, pc.pst v pc.pst etc). The user can also select one .pspi file and then select the .pst files or directories contained in that .pspi file and compare them. Furthermore 
two .pspi files can be selected and the .pst files or pst directories contained in the .pspi files can be compared. However the user cannot select two pspi files and compare them directly, the user must 
choose specific files contained inside the .pspi file. The program will automatically create a Report directory in the same location where the jar file is located where the reports will be created if one 
does not alreay exist. The comparator also creates a temp directory that is used so that the original files are not altered in any way. I suggest using ctrl + f when reading the reports to find the specifc
preset you are looking for.

If the comparator jar is not running correctly you might need to move it to a new directory, however this is a very specific case and if left on the desktop or some other empty directory the jar file 
runs perfectly. Currently if two files of any other type other than those described above the program will do nothing.

