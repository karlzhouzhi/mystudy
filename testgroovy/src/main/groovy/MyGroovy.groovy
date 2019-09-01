import groovy.util.slurpersupport.GPathResult

class MyGroovy{
    static void main(String[] args){
        def x = 1
        def var2 = "i am a person"
        def int y = 2

        println("this is my groovy")
        println(x)
        println(var2)
        println(y)
        println("----------------------")

        println(testFun(1,2))
        println(nonReturnTypeFun())

        println('-------------------------')

        def myStr = 'this is  $x dollar'
        def myStr2 = "this is $x dollor"
        println(myStr)
        println(myStr2)

        println('------------------------')
        println('canonical name: ' + x.getClass().getCanonicalName())

        def aList = ['hello', 1.2, true]
        aList[100] = 100
        println(aList[0])
        println("size: " + aList.size())

        def  aMap = ['key1' : true, 'key2' : "i am $x dollar"]
        println(aMap.key1)
        println(aMap['key2'])
        aMap.annother = 1.2
        for (String key : aMap.keySet()){
            println("key: " + key  + ", value: " + aMap.get(key))
        }

        for (Map.Entry<String, Object> entry : aMap.entrySet()){
            println("key: " + entry.key + ", value: " + entry.value)
        }

        def aRange = 1..5
        for (int i=aRange.from; i<=aRange.to; i++){
            println(i)
        }

        def aClosure = {
            arg1, arg2 ->
                def result = arg1 + arg2
                println("result: "+ result)
        }
        aClosure.call(1, 2)

//        def greeting = {"hello, $it"}
        def greeting = {it -> "hello, $it"}
        println(greeting('karl'))

        def myList = [1,2,3,4]
        myList.each {
            println(it)
        }

        testClouse(1, "hello", {
            println("i am in closure")
        })


        def targetFile = new File('D:\\personal\\work_space\\Android\\as3.3\\MyStudy\\testgroovy\\src\\main\\AndroidManifest.xml')
//        def targetFile = new File('mytest.groovy')
        targetFile.eachLine {
            oneLine ->
                println oneLine
        }
        println(targetFile.getBytes())

//        def newFile = new File('mysrc.txt')
//        newFile.createNewFile()

        /*def srcFile = new File('mysrc.txt')
        def tagetFile = new File('mytarget.txt')
        tagetFile.withOutputStream {os ->
            srcFile.withInputStream {ins ->
                os << ins
            }
        }*/

        parseXml()

//        assert aList[5] = null
//        aList[100] = 100
//        assert aList[100] == 100
//        println("size: " + aList.size())

//        testList()
    }

    static def testList(){
//        def aList = ['hello', 1.2, true]
//        asset aList[1] == 'hello'
//        assert aList[5] = null
//        aList[100] = 100
//        assert aList[100] == 100
//        println("size: " + aList.size())
//
//        "10"
    }

    static String testFun(arg1, arg2){
        return "this if from testFun return value"
    }

    static def nonReturnTypeFun(){
        20
    }

    static def testClouse(int a, String s1, Closure closure){
        closure()
    }

    static def parseXml(){
        def xparser = new XmlSlurper()
        def targetFile = new File('test.xml')

        GPathResult gPathResult = xparser.parse(targetFile)

        def book = gPathResult.value.books.book[3]
        def author = book.author
        def title = book.title

        println("book id: " + book['@id'] + ", available： " + book['@available'])
        println("author: " + author + ", title: " + title + ", author id: " + author.@id)

        def mainManifest = new File('app/src/main/AndroidManifest.xml')
//        println(mainManifest.getBytes())
//        mainManifest.eachLine {oneLine ->
//            print(oneLine)
//        }

        def xparser2 = new XmlSlurper()
        GPathResult gPathResult1 = xparser2.parse(mainManifest)

        def appLabel = gPathResult1.manifest.application['@android:label']
        def activity0 = gPathResult1.manifest.application.activity[0]
        def activity0Name = activity0.@'android:name'

        println("app label: " + appLabel + ", activity0Name: " + activity0Name)

    }
}