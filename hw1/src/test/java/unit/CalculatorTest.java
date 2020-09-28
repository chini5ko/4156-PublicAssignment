package unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import models.Calculator;
import org.junit.jupiter.api.Test;


public class CalculatorTest {

  Calculator cal = new Calculator();
  
  @Test
  public void testAdd() {
    
    int num1 = 5;
    int num2 = 5;
    
    int sum = cal.add(num1, num2);
    
    assertEquals(10, sum);
  }
  
  @Test
  public void testDivNonZeroDenominator() {
    
    int num1 = 5;
    int num2 = 5;
    
    double division = cal.division(num1, num2);
    
    assertEquals(1, division);
  }
  
  @Test
  public void testDivZeroDenominator() {
    
    int num1 = 5;
    int num2 = 0;
    
    double division = cal.division(num1, num2);
    
    assertEquals(Double.NaN, division);
  }
}