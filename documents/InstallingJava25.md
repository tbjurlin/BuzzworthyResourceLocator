## Installing java 25 lts on the vm

1. Go to https://www.oracle.com/java/technologies/downloads/ and download the x64 compressed archive for linux.
2. Extract the archive with `tar -xzf <archive name>`.
3. Move the extracted folder to the jvm folder with `sudo mv <extracted folder> /usr/lib/jvm/oracle-jdk-25`
4. Install the jdk with `sudo update-alternatives --install "usr/bin/java" "java" "/usr/lib/jvm/oracle-jdk-25/bin/java" 1`.
5. Switch to the installed java with `sudo update-alternatives --config java` and select the java version `/usr/lib/jvm/oracle-jdk-25/bin/java`.
5. Verify that you have properly installed java with `java --version`. The resoponse should be something like `java 25 2025-09-16 LTS`.