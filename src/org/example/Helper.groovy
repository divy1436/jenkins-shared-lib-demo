package org.example

class Helper {
    static String toUpper(String name){
        
        def n = name ?: 'there' 
        return n.toUpperCase()
    }
    static String readGreeting(){
        return libraryResource('org/example/greeting,txt').trim()
    }
}
