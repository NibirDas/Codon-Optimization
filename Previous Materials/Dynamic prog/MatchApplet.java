
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

public class MatchApplet extends Applet {

  TextField seq1in = new TextField("HEAGAWGHEE");
  TextField seq2in = new TextField("PAWHEAE");
  Button alignButton = new Button("Compute alignment");
  TextArea outa = new TextArea(15, 80);

  public void init() {
    // Set font to Courier
    Font courier = new Font("Courier", Font.PLAIN, 12);
    seq1in.setFont(courier); seq2in.setFont(courier); outa.setFont(courier); 
    // Lay out the input fields above the button above the output field
    setLayout(new BorderLayout());
    Panel input = new Panel();
    input.setLayout(new BorderLayout());
    input.add(seq1in, "North"); 
    input.add(seq2in, "Center"); 
    Panel butpanel = new Panel();
    butpanel.add(alignButton);
    input.add(butpanel, "South");
    add(input, "North");
    add(outa, "Center");

    final Substitution sub = new Blosum50();
    alignButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	String seq1 = seq1in.getText(), seq2 = seq2in.getText();
	Output out = new Output () {
	  public void print(String s)
	  { outa.append(s); }
	  
	  public void println(String s)
	  { outa.append(s); outa.append("\n"); }
	  
	  public void println()
	  { outa.append("\n"); }
	};

	outa.setText("");
      (new NW      (sub, 8,     seq1, seq2)).domatch(out, "GLOBAL ALIGNMENT");
      
      }
    });
  }
}
