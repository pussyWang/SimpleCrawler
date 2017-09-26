# -*- coding:utf-8

import requests

hea = {
    'Accept': "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    'Accept-Encoding': 'gzip, deflate, br',
    'Accept-Language': 'zh-CN,zh;q=0.8,en;q=0.6',
    'Connection': 'keep-alive',
    'Content-Type': 'application/x-www-form-urlencoded',
    'Host': 'www.baidu.com',
    'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) '
                 'AppleWebKit/537.36 (KHTML, like Gecko)'
                 ' Chrome/56.0.2924.87 Safari/537.36'
}
url = 'http://www.baidu.com'
params = ''
html = requests.get(url, headers=hea, verify=False)
print html.text