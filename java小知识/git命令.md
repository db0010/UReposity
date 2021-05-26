- 新建git仓库

  - mkdir来创建目录，在用git init命令初始化该目录为git仓库，成功了会生成一个.git文件，里面存放的时仓库的版本信息

- 移动文件到仓库里面

- 添加文件到暂存区：git add 文件名

- 提交文件到仓库：git commit -m “版本的说明提示”

- 查看仓库的全部版本日志：git log

- 查看仓库的状态：git status

- 查看仓库的所有更改操作的命令日志：git reflog

- 回滚仓库版本：git reset --hard HEAD^(提示：这里的^表示回滚一个版本，^^表示回滚2个版本，往上100个版本写100个^比较容易数不过来，所以写成HEAD~100)；在知道版本号之后用git reset --hard 8001bb9ab318763071ad072d47e49111601d117c命令可以回到指定的版本，版本号是十六进制的一串数字

- git diff HEAD -- readme.txt命令可以查看工作区和版本库里面最新版本的区别

- 撤销修改：下面的两种情况下，使用git checkout -- test.txt命令，可以撤销对文件的修改

  - 当文件被add到暂存区了之后，并且做出了修改
  - 文件被修改了之后还没有被add到暂存区

- 撤销暂存区的文件到工作区（就是修改了文件为add到暂存区时）：使用git reset HEAD test.txt

- 删除文件

  - 可以用rm t.txt，然后git add t.txt进暂存区，再git commit -m "注释"提交；这样在仓库中就删除了t.txt文件
  - 还可以用git rm t.txt，然后提交git commit -m "注释"提交；这样相比上面少了add进暂存区的步骤

- 远程仓库

  - 在本地仓库和github上都新建一个名称相同的仓库（名称不一样的似乎不能传数据）

  - 第一次在本机上使用git的远程仓库时，需要有个.ssh文件，里面都私钥和公钥两个文件，用下面的命令生成.ssh文件，在github上也需要ssh设置，把公钥复制过去

    ```bash
    ssh-keygen -t rsa -C "2587468048@qq.com"
    ```

  - 再建立本地仓库与远程仓库的连接

    ```bash
    //origin表示是远程仓库的同一名称，因为只与远程仓库有直接关联
    git remote add origin git@github.com:db0010/test.git
    ```

  - 现在就可以向远程仓库push了，表示把本地仓库的文件都更新到远程仓库里

    ```bash
    git push origin master//第一次用git push -u origin master
    ```

  - 删除该本地仓库与远程仓库的关系

    ```bash
    //先看查看是否连接远程仓库
    git remote -v
    git remote rm origin
    ```

  - 