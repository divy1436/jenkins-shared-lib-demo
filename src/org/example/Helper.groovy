package org.example

class Helper {
    // Add 'String name' or just 'name' inside the brackets
    static def toUpperCase(name) { 
        def n = name ?: 'there' 
        return n.toUpperCase()
    }
}
