import org.example.Helper

def call(String name = 'there'){
 def greeting = Helper.readGreeting()
 echo "${greeting} $(upperName}!"
}
