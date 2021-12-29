import cv2
import face_recognition
import os
import numpy as np
import pyrebase
from datetime import date
from datetime import datetime
import calendar
import smtplib


def removeFirstAndLast(val):
    temp = ""
    for idx in range(2, len(val) - 1):
        temp += val[idx]
    return temp


def escapeDetails(rollno):
    res = ""
    for student in stdDetails:
        if str(rollno) == removeFirstAndLast(student[0]):
            res += day + "*"
            temp = student[1].split(",")[6].split(": ")[1]
            res += temp[1:-2] + "*"
            temp = student[1].split(",")[0].split(": ")[1]
            res += temp[1:-1] + "*"
            temp = student[1].split(",")[5].split(": ")[1]
            res += temp[1:-1] + "*" + str(tm)
    return res


def timeToNumber(time):
    hrs = int(time.split(":")[0])
    mns = int(time.split(":")[1])
    return hrs * 60 + mns


def sendMail(rollno):
    esc = escapeDetails(rollno).lower()
    escDay = esc.split("*")[0]
    escYear = esc.split("*")[1]
    escBranch = esc.split("*")[2]
    escSec = esc.split("*")[3]
    escTime = esc.split("*")[4]
    for facD in facDetails:
        facDay = facD[0].split("*")[0][1:]
        facYear = facD[0].split("*")[1]
        facBranch = facD[0].split("*")[2]
        facSec = facD[0].split("*")[3]
        facSTime = timeToNumber(facD[0].split("*")[4])
        facETime = timeToNumber(facD[0].split("*")[5][:-1])
        if escDay == facDay and escYear == facYear and escBranch == facBranch and escSec == facSec:
            if facSTime <= int(escTime) <= int(facETime):
                facMail = facD[1].split(",")[0].split(": ")[1][1:-1]
                sender = "19r21a1298@mlrinstitutions.ac.in"
                password = "Druva@123"
                subject = "Student Detection"
                body = rollno + " is out of class from 10min\nDetails: \nYear = " + escYear + "\nBranch = " + escBranch + " " + escSec + "\n"

                message = f"""From:{sender}
                To:{facMail}
                Subject:{subject}\n
                {body}"""

                server = smtplib.SMTP("smtp.gmail.com", 587)
                server.starttls()

                try:
                    server.login(sender, password)
                    print("Logged in to mail")
                    server.sendmail(sender, facMail, message)
                    print("Email has been Sent Successfully!")
                except smtplib.SMTPAuthenticationError:
                    print("Unable to Sign in!")


curr_date = date.today()
day = str(calendar.day_name[curr_date.weekday()])

now = datetime.now()
hr, mn = map(int, now.strftime("%H %M").split())
tm = hr * 60 + mn

config = {
    "apiKey": "AIzaSyA3mDWmb1tp6rZW3QZ3oSog7n2MCHiyEhU",
    "authDomain": "escape-bulletin.firebaseapp.com",
    "databaseURL": "https://escape-bulletin-default-rtdb.firebaseio.com",
    "projectId": "escape-bulletin",
    "storageBucket": "escape-bulletin.appspot.com",
    "messagingSenderId": "676021577156",
    "appId": "1:676021577156:web:e33573ad7b0ff3ebe9b091",
    "measurementId": "G-F0GENYBKX1"
}

firebase = pyrebase.initialize_app(config)
database = firebase.database()
storage = firebase.storage()

students = database.child("Student").get()
faculty = database.child("Faculty").get()

stdDetails = []
facDetails = []
timings = []

for std in students.each():
    data = str(std.val())
    rollNos = [data.split(",")[4].split(":")[1], data]
    stdDetails.append(rollNos)

for fac in faculty.each():
    data = str(fac.val())
    timeTable = [str(data.split(",")[1].split(": ")[1]).lower(), str(data).lower()]
    tempSTimings = str(data.split(",")[1].split(": ")[1].split("*")[4])
    tempETimings = str(data.split(",")[1].split(": ")[1].split("*")[5])
    timings.append([str(tempSTimings), str(tempETimings[0:-1])])
    facDetails.append(timeTable)

print(facDetails)
print(timings)

path = 'Images'
images = []
Names = []
myList = os.listdir(path)

NamesSet = {"0"}

for cl in myList:
    Names.append(os.path.splitext(cl)[0])
    NamesSet.add(str(os.path.splitext(cl)[0]))
NamesSet.remove("0")

for std in stdDetails:
    if str(std[0]) in str(NamesSet):
        continue
    string = ""
    for i in range(2, len(std[0]) - 1):
        string += std[0][i]
    path_on_cloud = "Student/" + string
    storage.child(path_on_cloud).download("Images/" + string + ".jpeg")

newList = os.listdir(path)
for cl in newList:
    curImg = cv2.imread(f'{path}/{cl}')
    images.append(curImg)


def findEncodings(images):
    encodeList = []
    for img1 in images:
        img1 = cv2.cvtColor(img1, cv2.COLOR_BGR2RGB)
        encode = face_recognition.face_encodings(img1)[0]
        encodeList.append(encode)
    return encodeList


print(stdDetails)

encodeListKnown = findEncodings(images)

cap = cv2.VideoCapture(0)

Dict = {}

while True:
    success, img = cap.read()
    imgS = img
    imgS = cv2.cvtColor(imgS, cv2.COLOR_BGR2RGB)
    facesCurFrame = face_recognition.face_locations(imgS)
    encodesCurFrame = face_recognition.face_encodings(imgS, facesCurFrame)
    for encodeFace, faceLoc in zip(encodesCurFrame, facesCurFrame):
        matches = face_recognition.compare_faces(encodeListKnown, encodeFace)
        faceDis = face_recognition.face_distance(encodeListKnown, encodeFace)
        matchIndex = np.argmin(faceDis)
        if matches[matchIndex]:
            rollNo = Names[matchIndex].upper()
            if rollNo not in Dict:
                Dict[rollNo] = 1
            else:
                Dict[rollNo] = Dict[rollNo] + 1

            if Dict[rollNo] >= 30:
                sendMail(rollNo)
                Dict[rollNo] = 0

            y1, x2, y2, x1 = faceLoc
            cv2.rectangle(img, (x1, y1), (x2, y2), (0, 0, 255), 2)
            cv2.rectangle(img, (x1, y2 - 35), (x2, y2), (0, 0, 255), cv2.FILLED)
            cv2.putText(img, rollNo, (x1 + 6, y2 - 2), cv2.FONT_HERSHEY_COMPLEX, 1, (0, 255, 255), 2)
    cv2.imshow('WebCam', img)
    cv2.waitKey(1)
