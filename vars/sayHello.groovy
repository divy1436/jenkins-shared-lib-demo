import org.example.Helper

def call(String name = 'there'){
def greeting = libraryResource('org/example/greeting,txt').trim()
 def upperName = Helper.readGreeting()
 echo "${greeting} $(upperName}!"
}
