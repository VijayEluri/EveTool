/*
 * Copyright (c) 2009 Chris Smith
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.co.md87.evetool.ui.dialogs.addaccount;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import uk.co.md87.evetool.AccountManager;

/**
 * Allows the user to add a new account.
 * 
 * @author chris
 */
public class AddAccountDialog extends JDialog implements ActionListener {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 10;

    private final JTextField userID, apiKey;

    private final JButton addButton, cancelButton;

    private final AccountManager manager;

    public AddAccountDialog(final Window owner, final AccountManager manager) {
        super(owner, "Add new account", ModalityType.APPLICATION_MODAL);

        this.manager = manager;

        setLayout(new MigLayout("wrap 2, fill", "[fill,grow 10|fill,grow 30,200]", "[fill]"));

        userID = new JTextField();
        apiKey = new JTextField();

        add(new JLabel("User ID:", JLabel.RIGHT));
        add(userID);

        add(new JLabel("API Key:", JLabel.RIGHT));
        add(apiKey);

        addButton = new JButton("Add");
        cancelButton = new JButton("Cancel");

        addButton.addActionListener(this);
        cancelButton.addActionListener(this);

        add(addButton, "span, split");
        add(cancelButton);

        setResizable(false);
        pack();

        setLocationRelativeTo(owner);
    }

    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == addButton) {
            final int enteredUserID = Integer.parseInt(userID.getText());
            final String enteredKey = apiKey.getText();

            manager.addAccount(enteredUserID, enteredKey);

            dispose();
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }

}
