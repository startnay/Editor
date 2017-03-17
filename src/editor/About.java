/**
 * @name        Simple Java NotePad
 * @package     ph.notepad
 * @file        UI.java
 * @author      SORIA Pierre-Henry
 * @email       pierrehs@hotmail.com
 * @link        http://github.com/pH-7
 * @copyright   Copyright Pierre-Henry SORIA, All Rights Reserved.
 * @license     Apache (http://www.apache.org/licenses/LICENSE-2.0)
 * @create      2012-05-04
 * @update      2015-09-4
 *
 *
 * @modifiedby  Achintha Gunasekara
 * @modweb      http://www.achinthagunasekara.com
 * @modemail    contact@achinthagunasekara.com
 */

package Editor;

import javax.swing.*;
import java.awt.FlowLayout;

public class About {

    private final JFrame frame;
    private final JPanel panel;
    private String contentText;
    private final JLabel text;

    public About() {
        panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(500,300);
        text = new JLabel();
    }

    public void me() {
        frame.setTitle("About Me - " + TextEditor.NAME);

        contentText =
        "<html><body><p>" +
        "Group: E1-Year3<br />" +
        "Contact Us at: " +
        "<a href='mailto:" + TextEditor.AUTHOR_EMAIL + "?subject=About the NotePad PH Software'>" + TextEditor.AUTHOR_EMAIL + "</a>" +
                "<br /><br />" +
                "Modified By: E1-Year3<br />" +
                "Contact Us at: <a href='mailto:" + TextEditor.EDITOR_EMAIL + "?subject=About the NotePad PH Software'>" + TextEditor.EDITOR_EMAIL + "</a>" +
        "</p></body></html>";

        text.setText(contentText);
        panel.add(text);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
    }

    public void software() {
        frame.setTitle("About Us - " + TextEditor.NAME);

        contentText =
        "<html><body><p>" +
        "Name: " + TextEditor.NAME + "<br />" +
        "Version: " + TextEditor.VERSION +
        "</p></body></html>";

        text.setText(contentText);
        panel.add(text);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
    }

}