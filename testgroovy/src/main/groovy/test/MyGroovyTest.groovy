package test

class MyGroovyTest{
    String name
    String pwd

    MyGroovyTest(String name, String pwd){
        this.name = name
        this.pwd = pwd
    }

    def print(){
        println("name: " + name + ", pwd: " + pwd)
    }
}