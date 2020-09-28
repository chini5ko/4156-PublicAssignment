package models;

public class Calculator {
  
  /**
   * Returns sum of two integers.
   * @param a First number to add
   * @param b Second number to add
   * @return Sum of a and b
   */
  public int add(int a, int b) {
    return a + b;
  }
  
  /**
   * Returns division of two integers. If denominator is zero, method returns NaN.
   * @param numerator 
   * @param denominator
   * @return Result of division
   */
  public double division(int numerator, int denominator) {
    
    double result = 0.0;
    
    if(denominator != 0) {
      result = numerator/denominator;
    }else {
      result = Double.NaN;
    }
    
    return result;
  }

}