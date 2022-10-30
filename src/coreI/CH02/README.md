# Install

```sh
# mac
brew install java11 # openjdk

# For the system Java wrappers to find this JDK, symlink it with
  sudo ln -sfn /opt/homebrew/opt/openjdk@11/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-11.jdk

# If you need to have openjdk@11 first in your PATH, run:
  echo 'export PATH="/opt/homebrew/opt/openjdk@11/bin:$PATH"' >> ~/.zshrc

# For compilers to find openjdk@11 you may need to set:
  export CPPFLAGS="-I/opt/homebrew/opt/openjdk@11/include"

# ubuntu
sudo apt install openjdk-8-jdk  # openjdk-11-jdk

java -version # 查看默认版本

sudo update-alternatives --config java  # 安装了多个Java版本，修改默认版本

sudo apt remove openjdk-8-jdk # 删除

# 推荐：vscode command shift p 输入：jdk，选择安装即可

# 查看javahome位置
/usr/libexec/java_home -V
```

- JAVA_HOME：指向JDK的安装目录，`Eclipse/NetBeans/Tomcat`等软件就是通过搜索JAVA_HOME变量来找到并使用安装好的JDK. `export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 写入 ~/.zshrc`
- JDK_HOME：`JDK_HOME: %JAVA_HOME%`

# java/javac

`javac` 是编译器，将整个 `.java` 文件编译成 `.class` 文件。

```bash
javac <options> <source files>
options:
				-d <directory>                    指定编译生成的类文件的输出目录
				-cp <path> , -classpath <path>    指明了.java文件里import的类的位置
				-sourcepath <path>                指定源文件位置
				-verbose                          输出有关编译器正在执行的操作的消息
```

`java` 是解释器，逐个解释运行 `.class` 文件。

```bash
java [-options] class [args...]           执行类
java [-options] -jar jarfile [args...]    执行jar文件

options:
				-cp <path>, -calsspath <path>     class文件所需要的所有类的package路径
				-D<name>=<value>                  设置系统变量
```
