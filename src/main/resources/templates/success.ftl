<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>${profile}数据库${project}任务成功通知</title>
</head>
<body>
<p style="color: cyan;font-size: large"><b>’任务流：${project}执行编号为：${execid} 已经执行成功！’</b></p>
<br>
开始时间：${startTime?number_to_datetime}
<br>
结束时间：${endTime?number_to_datetime}
<br>
运行时间：${duration}
<br>
状态：${status}
<br>
</body>
</html>