#!/usr/bin/python
import os, sys
import cgi
import cgitb
import logging
cgitb.enable()
logging.basicConfig(level=logging.DEBUG)
print "Content-type: text/html\r\n\r\n"

form = cgi.FieldStorage()
for key in form.keys():
    logging.info(key)


print '<html>'
print '<head>'
print '<title>Hello Word - First Python CGI Program</title>'
print '</head>'
print '<body>'
print '<b>'
for key in form.keys():
    print key,
    print ":",
    print form.getvalue(key),
    print ","
print '</b>'
print '</body>'
print '</html>'