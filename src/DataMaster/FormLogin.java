package DataMaster;

import DBConnect.DBConnect;
import Dashboard.DashboardAdmin;
import Dashboard.DashboardKasir;
import Dashboard.DashboardManajer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class FormLogin extends JFrame {
    private JTextField txtUsername;
    private JButton CANCELButton;
    private JButton LOGINButton;
    private JCheckBox Checkbox_Password;
    private JPanel JPLogin2;
    private JPanel pnLvl1;
    private JPanel pnlLvl2;
    private JPasswordField passwordField1;
    public JPanel JPLogin;
    private JFrame frame;
    DBConnect connection = new DBConnect();
    String username;
    String password;
    String value;

    public void FrameConfig(){
        add(this.JPLogin);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public FormLogin(){
        FrameConfig();
        LOGINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = txtUsername.getText();
                password = passwordField1.getText();
                try {
                    connection.stat = connection.conn.createStatement();
                    String query = "SELECT * FROM Karyawan WHERE Username ='" + username + "' AND Password ='" + password + "' AND Status = 1";
                    connection.result = connection.stat.executeQuery(query);
                    if (connection.result.next()) {
                        try {
                            String value = connection.result.getString("Id_Karyawan");
                            String id = connection.result.getString("Id_Jabatan");
                            if (id.equals("JK0001")) {
                                JOptionPane.showMessageDialog(null, "Selamat datang Bapak/Ibu Manager!");
                                dispose();
                                DashboardManajer p = new DashboardManajer();
                                p.JPDashboardManajer.setVisible(true);
                            } else if (id.equals("JK0002")) {
                                JOptionPane.showMessageDialog(null, "Selamat datang Bapak/Ibu Admin!");
                                dispose();
                                DashboardAdmin p = new DashboardAdmin();
                                p.JPDashboardAdmin.setVisible(true);
                            } else if (id.equals("JK0003")) {
                                JOptionPane.showMessageDialog(null, "Selamat datang Bapak/Ibu Kasir!");
                                dispose();
                                DashboardKasir p = new DashboardKasir(value);
                                p.JPDashboardKasir.setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(null, "\nAnda tidak memiliki hak akses login !");
                            }
                        } catch (SQLException ex) {
                            System.out.println(ex);
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Inputkan Username & Password yang Benar!");
                    }

                } catch (SQLException ex) {

                }
            }
        });
        Checkbox_Password.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Checkbox_Password.isSelected() == true){
                    passwordField1.setEchoChar('\0');
                }else {
                    passwordField1.setEchoChar('*');
                }
            }
        });
        CANCELButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Terima Kasih");
                System.exit(0);
            }
        });
    }
}