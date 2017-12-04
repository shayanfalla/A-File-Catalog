# File-Catalog

This is a server program implemented with Java RMI which acts as a file catalog from which
a client can store their files, or that was what it was supposed to do.
The assignment got scaled down so handling metadata of a file was enough.

Enjoy!

```
Welcome to the file catalog!

Enter username('quit' to exit): Alex

Enter password: alex123

That username did not exist.
An account has been created with the credentials given.

Enter command: ('help' for list of commands)
list

FILE: again               OWNER:shayan              PERMISSIONS: private             READ/WRITE: read
FILE: file2               OWNER:olle                PERMISSIONS: public              READ/WRITE: write
FILE: bangers             OWNER:jag                 PERMISSIONS: public              READ/WRITE: write
FILE: myclientfile        OWNER:me                  PERMISSIONS: private             READ/WRITE: read
FILE: wr                  OWNER:me                  PERMISSIONS: private             READ/WRITE: read
FILE: filewithuser        OWNER:shayan              PERMISSIONS: public              READ/WRITE: read
FILE: newname             OWNER:me                  PERMISSIONS: private             READ/WRITE: write

Enter command: ('help' for list of commands)
help

List of commands:
upload <file> <public/private> <read/write>
modify <filename> <attribute> <new modification>
quit
list
logout
unregister

Enter command: ('help' for list of commands)
modify file2 filename foofile

file2 has changed name to foofile.

Enter command: ('help' for list of commands)
list

FILE: again               OWNER:shayan              PERMISSIONS: private             READ/WRITE: read
FILE: foofile             OWNER:olle                PERMISSIONS: public              READ/WRITE: write
FILE: bangers             OWNER:jag                 PERMISSIONS: public              READ/WRITE: write
FILE: myclientfile        OWNER:me                  PERMISSIONS: private             READ/WRITE: read
FILE: wr                  OWNER:me                  PERMISSIONS: private             READ/WRITE: read
FILE: filewithuser        OWNER:shayan              PERMISSIONS: public              READ/WRITE: read
FILE: newname             OWNER:me                  PERMISSIONS: private             READ/WRITE: write

Enter command: ('help' for list of commands)
unregister

You have been deregistered.
```
