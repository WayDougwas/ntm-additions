import javax.imageio.ImageIO; import java.awt.*; import java.awt.geom.*; import java.awt.image.BufferedImage; import java.io.File;
public class Logo { public static void main(String[] a) throws Exception {
  BufferedImage src = ImageIO.read(new File(a[0]));
  int N = 512; double k = N / 1024.0;
  BufferedImage img = new BufferedImage(N, N, BufferedImage.TYPE_INT_ARGB);
  Graphics2D g = img.createGraphics();
  g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
  g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
  g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
  g.drawImage(src, 0, 0, N, N, null);
  // circle centre in the original: (512, 708), radius ~58
  double cx = 512 * k, cy = 708 * k, r = 52 * k;
  Color yellow = new Color(0xFFD21E);
  // soft glow behind the trefoil
  for (int i = 12; i > 0; i--) {
    float al = 0.035f * (13 - i);
    g.setColor(new Color(1f, 0.82f, 0.12f, Math.min(1f, al * 0.6f)));
    double gr = r + i * 4 * k;
    g.fill(new Ellipse2D.Double(cx - gr, cy - gr, gr * 2, gr * 2));
  }
  g.setColor(new Color(0x0B0E17));
  g.fill(new Ellipse2D.Double(cx - r, cy - r, r * 2, r * 2));
  // radiation trefoil
  g.setColor(yellow);
  double ro = r * 0.92, ri = r * 0.30, hub = r * 0.18;
  for (int i = 0; i < 3; i++) {
    double start = 90 + i * 120 - 30; // blades centred at 90, 210, 330 (one pointing up)
    Area blade = new Area(new Arc2D.Double(cx - ro, cy - ro, ro * 2, ro * 2, start, 60, Arc2D.PIE));
    blade.subtract(new Area(new Ellipse2D.Double(cx - ri, cy - ri, ri * 2, ri * 2)));
    g.fill(blade);
  }
  g.fill(new Ellipse2D.Double(cx - hub, cy - hub, hub * 2, hub * 2));
  // keep the white ring of the original logo around it
  g.setColor(Color.WHITE);
  g.setStroke(new BasicStroke((float) (12 * k)));
  double rr = 58 * k;
  g.draw(new Ellipse2D.Double(cx - rr, cy - rr, rr * 2, rr * 2));
  // "+" badge (Additions) in the top-right corner
  double bx = 400 * k * 2 - 0, by = 112 * k * 2; // centre around (800,224) in 1024 space
  double br = 92 * k;
  g.setColor(new Color(0x0B0E17));
  g.fill(new Ellipse2D.Double(bx - br - 6 * k, by - br - 6 * k, (br + 6 * k) * 2, (br + 6 * k) * 2));
  g.setColor(yellow);
  g.fill(new Ellipse2D.Double(bx - br, by - br, br * 2, br * 2));
  g.setColor(new Color(0x0B0E17));
  double arm = br * 0.62, th = br * 0.30;
  g.fill(new RoundRectangle2D.Double(bx - arm, by - th / 2, arm * 2, th, th * 0.5, th * 0.5));
  g.fill(new RoundRectangle2D.Double(bx - th / 2, by - arm, th, arm * 2, th * 0.5, th * 0.5));
  g.dispose();
  ImageIO.write(img, "png", new File(a[1]));
} }
