def call(String name) {
    def greeting = "Hello"
    def upperName = name.toUpperCase()
    // FIXED: Correct curly brace syntax
    echo "${greeting} ${upperName}!"
}
