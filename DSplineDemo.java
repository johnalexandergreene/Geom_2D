package org.fleen.geom_2D;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class DSplineDemo extends JFrame{
  
  public static final void main(String[] a){
    splinedemo=new DSplineDemo();
  }
  
  static DSplineDemo splinedemo;
  
  //doubling a point makes the curve stick better
  static double[][] points={
    {100,400},
    {100,300},
    
    {200,250},
    
    {100,200},
    {100,100},
    
    {300,100},
    {300,200},
    
    {400,250},
    
    {300,300},
    {300,400}
  };
  
  public DSplineDemo(){
    
    setVisible(true);
    setBounds(100,100,700,700);
    
    
    
  }
  
  public void paint(Graphics g){
    //create the spline
    DSpline spline=new DSpline(points);
    double[] p;
    Path2D path=new Path2D.Double();
    for(double i=0;i<1;i+=0.01){
      p=spline.getPoint(i);
      if(i==0)
        path.moveTo(p[0],p[1]);
      else
        path.lineTo(p[0],p[1]);}
    //stroke it
    Graphics2D g2d=(Graphics2D)g;
    g2d.setStroke(new BasicStroke(2.0f));
    g2d.setPaint(Color.black);
    g2d.draw(path);
    //render points
    g2d.setPaint(Color.red);
    Ellipse2D e=new Ellipse2D.Double(0,0,5,5);
    double dotspan=6;
    for(double[] a:points){
      e.setFrame(a[0]-dotspan/2,a[1]-dotspan/2,dotspan,dotspan);
      g2d.fill(e);}}
  
}
