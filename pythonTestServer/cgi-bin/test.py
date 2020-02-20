#!/usr/bin/python
import os, sys
import cgi
import cgitb
import logging
cgitb.enable()
logging.basicConfig(level=logging.DEBUG)
print "Content-type: text/html\r\n\r\n"

form = cgi.FieldStorage()

test5 = form.getvalue('reason')
test4 = form.getvalue('version')
test3 = form.getvalue('name')
test2 = form.getvalue('code')
test1 = form.getvalue('policy')

print '<html>'
print '<head>'
print '<title>Hello Word - First CGI Program</title>'
print '</head>'
print '<body>'
print '<b>' + test1 + test2 + test3 + test4 + '</b>'
print '</body>'
print '</html>'

logging.info('test!!')
logging.info('reason' + test5)
