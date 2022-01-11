/*
 * Nama      : Fauzyah Hadirahma
 * NPM       : 20191310070
 * Tanggal   : 30 Agustus 2021
 * Ujian Akhir Semester
 */
package triangle.pyramid.animation;

/**
 *
 * @author Fauzyah
 */

import java.awt.event.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TPA3DTest extends JFrame 
  implements MouseListener,MouseMotionListener,
  MouseWheelListener {
    public static TPA3DTest tpa3dtest;
    
    public static void main(String[] args) {
        tpa3dtest = new TPA3DTest();
        tpa3dtest.setVisible(true);
    }
    
    public int width = 500;
    public int height = 500;
    public JPanel panel;
    public float distance;
    public float angle;
    Point prevMove = new Point();
    public Pyramid pyramid;
    
    public TPA3DTest() {
        super("3D Animasi Triangle Pyramid");
        
        // view setup
        angle = (float) Math.toRadians(90);
        distance = (width / 2) / (float) (Math.tan(angle / 2));
        System.out.println("distance : " + distance);
        
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                pyramid.project(g);
            }
        };
        
        panel.setPreferredSize(new Dimension(700, 700));
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        
        pyramid = new Pyramid();
    }
    
    class Pyramid {
        public int size = 120;
        Vector3D ulf, urf, llf, lrf; /* upper left front, upper right front, 
         *                              lower left front, lower right front */
        Vector3D ulb, urb, llb, lrb; // upper left back, ...
        
        public Pyramid() {
            int startx = width / 2 - size / 2;
            int starty = height / 2 - size / 2;
            
            ulf = new Vector3D(startx, starty, 0);
            urf = new Vector3D(startx + 68, starty + 60, 0);
            llf = new Vector3D(startx + size, starty - size, 0);
            
            ulb = new Vector3D(startx, starty, -size);
            urb = new Vector3D(startx, starty, -size);
            llb = new Vector3D(startx, starty, -size);
        }
        
        public void move(int dx, int dy) {
            ulf.x += dx;
            urf.x += dx;
            llf.x += dx;
            ulb.x += dx;
            urb.x += dx;
            llb.x += dx;
            
            ulf.y += dy;
            urf.y += dy;
            llf.y += dy;
            ulb.y += dy;
            urb.y += dy;
            llb.y += dy;
        }
        
        public void project(Graphics g) {
            Point pulf = ulf.to2D();
            Point purf = urf.to2D();
            Point pllf = llf.to2D();
            Point pulb = ulb.to2D();
            Point purb = urb.to2D();
            Point pllb = llb.to2D();
            
            g.setColor(Color.PINK);
            g.drawLine(pulf.x, pulf.y, purf.x, purf.y);
            g.drawLine(pulf.x, pulf.y, pllf.x, pllf.y);
            g.drawLine(purf.x, purf.y, pllf.x, pllf.y);
            
            g.setColor(Color.BLUE);
            g.drawLine(pllb.x, pllb.y, pllf.x, pllf.y);
            g.drawLine(pulf.x, pulf.y, pulb.x, pulb.y);
            g.drawLine(purb.x, purb.y, purf.x, purf.y);
        }
        
        float zoomFactor = 5;
        public void further() {
            ulf.z -= zoomFactor;
            urf.z -= zoomFactor;
            llf.z -= zoomFactor;
            ulb.z -= zoomFactor;
            urb.z -= zoomFactor;
            llb.z -= zoomFactor;
        }
        
        public void closer() {
            ulf.z += zoomFactor;
            urf.z += zoomFactor;
            llf.z += zoomFactor;
            ulb.z += zoomFactor;
            urb.z += zoomFactor;
            llb.z += zoomFactor;
        }
    }
    
    private float rotX, rotY, rotZ = 0f;
    
    class Vector3D {
        public float x, y, z;
        
        public Vector3D(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        public String toString() {
            return"(" + x + "," + y + "," + z + ")";
        }
        
        public Point to2D() {
            Vector3D v = new Vector3D(x, y, z);
            rotateVector(v, rotY, -rotX, rotZ);
            
            Point p;
            // 3d -> 2d
            float z = distance + v.z;
            p = new Point((int) (distance * v.x / z), 
                    (int) (distance * v.y / z));
            p.x += width / 2;
            p.y += height / 2;
            
            return p;
        }
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.getWheelRotation() < 0) {
            pyramid.closer();
            panel.repaint();
        } else {
            pyramid.further();
            panel.repaint();
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        prevMove = e.getPoint();
    }
    
    float ROT_FACTOR = 100;
    @Override
    public void mouseDragged(MouseEvent e) {
        int dx = e.getX() - prevMove.x;
        int dy = e.getY() - prevMove.y;
        
        if(e.isAltDown()) {
            if(e.isShiftDown()) {
                rotX += dx / ROT_FACTOR;
                rotY += 0;
                rotZ += dy / ROT_FACTOR;
            } else {
                rotX += dx / ROT_FACTOR;
                rotY += dy / ROT_FACTOR;
                rotZ += 0;
            }
        } else {
            pyramid.move(dx, dy);
        }
        
        panel.repaint();
        prevMove = e.getPoint();
    }
    
    public void rotateVector(Vector3D p, float thetaX, 
            float thetaY, float thetaZ) {
        
        float aX,aY, aZ;
        
        float camX = 0;
        float camY = 0;
        float camZ = 0;
        
        aX = p.x;
        aY = p.y;
        aZ = p.z;
        
        aY = p.y;
        aZ = p.z;
        p.y = (float) ((aY - camY) * Math.cos(thetaX)
                - (aZ - camZ) * Math.sin(thetaX));
        p.z = (float) ((aY - camY) * Math.sin(thetaX)
                - (aZ - camZ) * Math.cos(thetaX));
        
        aX = p.x;
        aZ = p.z;
        p.x = (float) ((aX - camX)* Math.cos(thetaY)
                + (aZ - camZ) * Math.sin(thetaY));
        p.z = (float) (-(aX - camX) * Math.sin(thetaY)
                + (aZ - camZ) * Math.cos(thetaY));
        
        aY = p.y;
        aX = p.x;
        p.x = (float) ((aX - camX) * Math.cos(thetaZ)
                - (aY -camY) * Math.sin(thetaZ));
        p.y = (float) ((aY - camY) * Math.cos(thetaZ)
                + (aX - camX) * Math.sin(thetaZ));
    }
}