// Start the REPL and load the file via>:load ImportExample.scala

val randomValue = { import scala.util.Random
     new Random().nextInt
   }

val randomValue2 = { import util.Random      
     new Random().nextInt                  
   }

