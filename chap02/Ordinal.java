// in chap02 run >javac Ordinal.java
// and then >java Ordinal 3

public class Ordinal {
  
  public static void main(String[] args) {
    ordinal(Integer.parseInt(args[0]));
  }
  
  public static void ordinal(int number) {
    switch(number) {
      case 1: System.out.println("1st"); break;
      case 2: System.out.println("2nd"); break;
      case 3: System.out.println("3rd"); break;
      case 4: System.out.println("4th"); break;
      case 5: System.out.println("5th"); break;
      case 6: System.out.println("6th"); break;
      case 7: System.out.println("7th"); break;
      case 8: System.out.println("8th"); break;
      case 9: System.out.println("9th"); break;
      case 10: System.out.println("10th"); break;
      default : System.out.println("Cannot do beyond 10");
    }
  }
}