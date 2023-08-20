# Product-of-Matrix-calculation-with-Amazon-Web-Service-connection
The Matrix calculation will be used Java programming language to implement, later on to configure the connection using Amazon Web Service instead of local host

The following setup is about configuring the AWS cloud service:

Region: US East (N. Virginia) us-east-1

Step 1: Create VPC Service

-> Click "Your VPC" then Click "Create VPC"
-> Resources to create: Click "VPC and more" instead of "VPC only"
-> Name tag auto-generation: Change the name to "mini-project".
-> Number of Availability Zones (AZs): Change it to 1
-> NAT gateways ($): Change it to "In 1 AZ" instead of "None".
-> VPC endpoints: Change it to "None" instead of S3 Gateway.
![image](https://github.com/HowardWHC/Product-of-Matrix-calculation-with-Amazon-Web-Service-connection/assets/106338557/f110d8c2-f0d1-421d-bb5f-e6a110f93b39)

Another setting that hasn't been mentioned, leave it by default setting.
---------------

Step 2: Create security group
Staying the same Services, click "Security groups" under "Security", then click "Create security group".

-> Security group name: set it to "mini_project" 
-> Description: Type "Allow user to SSH and run Java program with host 42210" // Just for identifying 
![image](https://github.com/HowardWHC/Product-of-Matrix-calculation-with-Amazon-Web-Service-connection/assets/106338557/a72258e6-36d1-46cf-bfa4-bad270c21a8b)
Add two Inbound rules:
Inbound rule 1:
Type: SSH (Protocol and Port range will be locked at TCP and 22)
Source type: Anywhere-IPv4
![image](https://github.com/HowardWHC/Product-of-Matrix-calculation-with-Amazon-Web-Service-connection/assets/106338557/a7d71710-25ae-4e2c-9ada-87e1cfab4281)
Inbound rule 2:
Type: Custom TCP
Port range: 42210
Source type: Anywhere-IPv4
![image](https://github.com/HowardWHC/Product-of-Matrix-calculation-with-Amazon-Web-Service-connection/assets/106338557/eb6d3c2c-7c7d-42f4-9261-0808d354d4ff)

Another setting that hasn't been mentioned, leave it by default setting.
----------------
Step 3: Launch EC2 instances

-> Click "Instances" under "Instances", then click "Launch instances".
-> Name: name "Mini_project"
-> Operating system: Amazon Linux
-> Amazon Machine Image (AMI): use "Amazon Linux 2 AMI (HVM) - Kernel 5.10, SSD Volume Type"
-> Instance type: choose "t2.large"
-> Key pair (login): vockey (Not worked outside the US East region)
-> Firewall (security groups) under Network: select "select existing security group" instead of "Create security group". At the same time, select "mini_project" (that's VPC created before) in Common security groups.

Another setting that hasn't been mentioned, leave it by default setting.
----------------

###
Go back Instance interface to check whether EC2 successfully created, Public IPv4 address is the address that will be used later on the command.
###

Step 4: In the user learner lab, download .pem file for ssh configuration.
![image](https://github.com/HowardWHC/Product-of-Matrix-calculation-with-Amazon-Web-Service-connection/assets/106338557/8a782815-a51b-4f50-aa73-c9c49c592578)

Step 5: Open command, and input the following command:
1. ssh -i C:\Users\Users\Desktop\labsuser.pem ec2-user@[IP address that EC2 created]
(This step is to access to EC2 instance from your directory)

2. sudo yum update
(To follow the AWS instruction to Run "sudo yum update" to apply all updates)

3. sudo amazon-linux-extras install java-openjdk11

4. sudo yum install java-11-openjdk-devel

5. scp -i C:\Users\[Your java file directory]\[java code name] ec2-user@ec2-xx-xx-xx-xx.compute-1.amazonaws.com:~/
(command "ls" to check if the file is located correctly)

6. javac -d . *.java
(Upload Java file, command "ls" to check if the file is located correctly also)

7. java a2223.hw1.student.[java "Server" file]

Step 6. Configure both Java "Server" and "Client" code:
public static final String DEFAULT_HOST = "[Your EC2 public IPv4 address created]"

Step 7. Run "Server" first and run "Client" afterward.
