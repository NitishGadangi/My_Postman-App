<img src="https://github.com/NitishGadangi/My_Postman-App/blob/master/ic_main.png?raw=true" align="right" height='250' />

# MyPostman App
📬 Android app with various advance features that enables you to Post JSON Data to a remote Api

This project is mainly aimed at making and app which establishes a connection between your backend api and info within our mobile.
In the above source the app constantly posts various details about the messages recieved in the form of Json which is grabed from the server end and used as per requirements.

## Whats happening in above app:
**REQUEST RAW DATA:**

POST /test.php HTTP/1.1
Host: somewebsitename.xyz
User-Agent: insomnia/6.3.2
Content-Type: application/json
Accept: */*
Content-Length: 145
Connection: keep-alive

```json
{
  "device_id":"xxxxxxxxxxxxxx",
  "imei":"00000000000000000",
  "sim":"1",
  "timestamp":"1560573718",
  "no":"00000000",
  "message":"This is a test message"
}
```

**RESPONSE RAW DATA:**
```
HTTP/1.1 200 OK
Content-Type: text/html; charset=UTF-8
Content-Length: 194
Date: Sat, 15 Jun 2019 04:51:56 GMT
Server: LiteSpeed
Proxy-Connection: keep-alive
```
```php
stdClass Object
(
    [device_id] => xxxxxxxxxxxxxx
    [imei] => 00000000000000000
    [sim] => 1
    [timestamp] => 1560573718
    [no] => 0000000000
    [message] => This is a test message
)
```

**NOTE:** THIS IS ONLY TEST DATA ON PRODUCTION CODE IT JUST RETURNS
```json
{"status": "success"}
```

## When does this occur?
**There is broadcast reciever running continously which looks for incomming messages.So that it make the Post Request every time a new message is recieved**
- Works even the device is locked.
- Option to add exemption to battery saver.
- Service gets paused when ever internet gets disconnected and resumes back when connection is back.
- Automatically starts in background as soon as mobile reboots or switches on.

**In this way there is a possiblity to add several other features to adjust it as per your requirements.**

### Code implementation instructions:
```
**This will be made available soon**
```
--------------------------------
**Thank you [flaticon.com](https://www.flaticon.com) for Awesome icon**
