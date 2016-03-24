package org.fleen.geom_2D;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import javax.swing.JFrame;

/*
 * WE USE THE SPLIT-TWEAK ALG
 */
public class CurveSmoother_Open{
  
  public double[][] getSmoothedOpenCurve(double[][] opencurve,int smoothness){
    double[][] curve=opencurve;
    for(int i=0;i<smoothness;i++)
      curve=doSplitTweak(curve);
    return curve;}
  
  private double[][] doSplitTweak(double[][] oldcurve){
    int 
      oldsize=oldcurve.length,
      newsize=oldsize*2-1;
    double[][] newcurve=new double[newsize][2];
    //copy points from oldcurve to new curve
    for(int i=0;i<oldsize;i++)
      newcurve[i*2]=new double[]{oldcurve[i][0],oldcurve[i][1]};
    //create midpoints
    for(int i=0;i<newsize-1;i++)
      if(i%2!=0)
        newcurve[i]=GD.getPoint_Mid2Points(
          newcurve[i-1][0],newcurve[i-1][1],newcurve[i+1][0],newcurve[i+1][1]);
    //move oldpoint to point between midpoints and oldpoint
    int imid0,imid1,iold;
    double[] midmid;
    for(int i=1;i<oldsize-1;i++){
      imid0=(i*2)-1;
      imid1=(i*2)+1;
      iold=i*2;
      midmid=GD.getPoint_Mid2Points(
        newcurve[imid0][0],newcurve[imid0][1],newcurve[imid1][0],newcurve[imid1][1]);
      newcurve[iold]=GD.getPoint_Mid2Points(
        midmid[0],midmid[1],newcurve[iold][0],newcurve[iold][1]);}
    //
    return newcurve;}
  
  
  //TEST  
  public static final void main(String[] a){
    
    @SuppressWarnings("serial")
    JFrame frame=new JFrame(){
      public void paint(Graphics g){
        
        double[][] curve={
          {50,50},
          {50,250},
          {250,250}};
        curve=new CurveSmoother_Open().getSmoothedOpenCurve(curve,3);
          
        Path2D path=new Path2D.Double();
        double[] p=curve[0];
        path.moveTo(p[0],p[1]);
        for(int i=1;i<curve.length;i++)
          path.lineTo(curve[i][0],curve[i][1]);
        
        Graphics2D g2=(Graphics2D)g;
        g2.setPaint(Color.black);
        g2.setStroke(new BasicStroke(2.0f));
        g2.draw(path);        
      }
    };
    
    frame.setSize(400,400);
    frame.setVisible(true);
    frame.repaint();}

}
