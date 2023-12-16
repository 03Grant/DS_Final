第一种方法：将Final_1_Server打包到此处之后，运行docker，使用docker build指令进行打包，之后可以放在服务器上进行运行

第二种方法：直接在服务器上运行：`docker push granthee/distributed_system:filereceiver`，得到已经上传的镜像。

运行镜像命令：
`docker run -v ~/fileWare:/app/fileWare -p 4177:4177 -d --name fileReceiver granthee/distributed_system:filereceiver`

该命令会将服务器的~/fileWare文件夹（请确认有这个文件夹）挂载到docker里的/app/fileWare文件夹，同时将TCP端口4177进行映射。

运行成功后，可以使用Final_1中的FileSender发送文件
