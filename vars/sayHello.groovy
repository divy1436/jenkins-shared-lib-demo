import org.example.Helper

def call(String name = 'there'){
 def upper = Helper.topUpper(name)
 echo "Hello ${upper}!"
}
