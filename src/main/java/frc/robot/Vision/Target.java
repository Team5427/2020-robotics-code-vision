package frc.robot.Vision;

import java.util.ArrayList;

import org.opencv.core.*;

public class Target {

    /**
     * The left and right pieces of retroreflective tape
     */
    /**
     * The average of both halves' centers
     */
    
    public boolean isHatch;
    /**
     * From Manual - In inches
     */

    Point[] points;

    private double topWidth;
    private Point center = new Point();
    private double widthRatio = 0;

    private double bottomPointVal = 0;
    private Point topLeftPoint = null;
    private Point topRightPoint = null;
    private Point bottomLeftPoint = null;
    private Point bottomRightPoint = null;
    private double leftValDiff = 0;
    private double rightValDiff = 0;
    private double proportion = 0;
    private double size = 0;
    private Mat mat;
    
    public Target(Point[] pts, Mat mat) 
    {
        this.mat = mat;
        points = new Point[pts.length];
        for(int x = 0; x<pts.length;x++)
        {
            points[x] = pts[x];
        }

        Point topLeftPoint = points[0];
        Point topRightPoint = points[0];
    
        bottomPointVal = points[0].y;
        for (Point p : points)
        {
            if(p.x < topLeftPoint.x)
                topLeftPoint = p;
            if(p.x > topRightPoint.x)
                topRightPoint = p;

            //think of it as fourth quadrant, y=0 is on top
            if(p.y > bottomPointVal)
            {
                bottomPointVal = p.y;
            }
        }

        center.y = ((topLeftPoint.y + bottomPointVal) / 2) + ((topRightPoint.y + bottomPointVal )/2)/2;
        center.x = (topLeftPoint.x + topRightPoint.x) / 2;

        leftValDiff  = Math.abs(topLeftPoint.y-bottomPointVal);
        rightValDiff = Math.abs(topRightPoint.y-bottomPointVal);

        proportion = leftValDiff/rightValDiff;
        size = (leftValDiff + rightValDiff)/2;

        //TODO FILTER IF THERE ARE MULTIPLE TARGETS
    }
    public double getWidthRatio()
    {
        return widthRatio;
    }
    public double getDistance()
    {
        // 2/9/20 distance model A with 1 outlier and somewhat evenly spaced out points
        // return (.029611)*(Math.pow(size,2)) + (-7.04515)*size + (472.515);
        // 2/9/20 distance model B with more points clustered under 150 inches
        return (.015631)*(Math.pow(size,2)) + (-4.49081)*size + (359.922);
    }
    public double getLeftValDiff()
    {
        return leftValDiff;
    }
    public double getRightValDiff()
    {
        return rightValDiff;
    }
    public double getProportion()
    {
        return proportion;
    }
    public double getSize()
    {
        return size;
    }
    public double gettopWidth()
    {
        return topWidth;
    }
    public Point getTopLeftPoint()
    {
        return topLeftPoint;
    }
    public Point getTopRightPoint()
    {
        return topRightPoint;
    }
    public Point getBottomLeftPoint()
    {
        return bottomLeftPoint;
    }
    public Point getBottomRightPoint()
    {
        return bottomRightPoint;
    }
    public double getDistanceFromCenter()
    {
        return center.x - mat.width()/2;
    }
    public boolean isCentered()
    {
        return Math.abs(getDistanceFromCenter())<21;
    }
   
    // public double getTapeDist()
    // {
    //     double diffX = right.topLeft.x - left.topLeft.x;
    //     double diffY = right.topLeft.y - left.topLeft.y;
    //     double pixDist = (right.topLeft.y != left.topLeft.y)? Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2)) : diffX;
    //     return pixDist;
    // }

    // public double getXOverZ() {
    //     return (center.x - GraphicsPanel.imageCenterX)/GraphicsPanel.focalLen;
    // }
    // public double getYOverZ() {
    //     return (center.y - GraphicsPanel.imageCenterY)/GraphicsPanel.focalLen;
    // }
    // public double getConstant3() {
    //     return getYOverZ()/getXOverZ();
    // }
    // public double getConstant4() {
    //     return 45; //inches, height of target - height of camera
    // }
    // public double solveForX() {
    //     return getConstant4()/getConstant3();
    // }
    // public double solveForZ() {
    //     return solveForX()/getXOverZ();
    // }
    // public double getHorAngle() {
    //     return Math.atan(solveForX());
    //}
    

}