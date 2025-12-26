package org.example

class Helper {
  static String toUpper(String name){
    def n = name ?: 'there'
    return n.toUpperCase()
  }
}
