# -*- coding:utf-8

import urllib2
import cookielib


# cookie = cookielib.CookieJar()
file = 'file/cookie.txt'
cookie = cookielib.MozillaCookieJar(file)
handler = urllib2.HTTPCookieProcessor(cookie)
opener = urllib2.build_opener(handler)
httphandler = urllib2.HTTPHandler(debuglevel=1)
# request = urllib2.Request("http://www.baidu.com")
# response = urllib2.urlopen(request)
response = opener.open('http://www.biandang.58.com')
for item in cookie:
    print 'Name = ' + item.name
    print 'Value = ' + item.value

cookie.save(ignore_discard=True, ignore_expires=True)


# print response.read()