package Dashboard;

import DataMaster.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardAdmin extends JFrame{
    private JButton btnAksesoris;
    private JButton btnSupplier;
    private JButton btnService;
    private JButton btnHandphone;
    private JButton btnMerk;
    private JButton btnJenisService;
    private JButton btnLogout;
    public JPanel JPDashboardAdmin;
    private JPanel PanelKiri;
    private JPanel PanelKiriAtas;
    private JPanel PanelButton;
    private JButton btnJabatan;
    private JButton btnLogOut;
    private JPanel PanelRole;
    private JPanel PanelTengah;
    private JPanel PanelForm;
    private JLabel lblJudul;
    private JButton btnKaryawan;
    private JPanel JPAdmin;

    public void FrameConfig(){
        add(this.JPDashboardAdmin);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public DashboardAdmin() {
        FrameConfig();
        btnAksesoris.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                MasterAksesoris show = new MasterAksesoris();
                show.JPAksesoris.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JPAksesoris);
                lblJudul.setText("MASTER AKSESORIS");
            }
        });
        btnSupplier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                MasterSupplier show = new MasterSupplier();
                show.JPSupplier.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JPSupplier);
                lblJudul.setText("MASTER SUPPLIER");
            }
        });
        btnService.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                MasterService show = new MasterService();
                show.JPService.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JPService);
                lblJudul.setText("MASTER SERVICE");
            }
        });
        btnHandphone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                MasterHandphone show = new MasterHandphone();
                show.JPHandphone.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JPHandphone);
                lblJudul.setText("MASTER HANDPHONE");
            }
        });
        btnMerk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                MasterMerk show = new MasterMerk();
                show.JPMerk.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JPMerk);
                lblJudul.setText("MASTER MERK");
            }
        });
        btnJenisService.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                MasterJenisService show = new MasterJenisService();
                show.JPJenisService.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JPJenisService);
                lblJudul.setText("MASTER JENIS SERVICE");
            }
        });
        btnKaryawan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                MasterKaryawan show = new MasterKaryawan();
                show.JPKaryawan.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JPKaryawan);
                lblJudul.setText("MASTER KARYAWAN");
            }
        });
        btnLogOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int opsi;
                opsi = JOptionPane.showConfirmDialog(null,"Apakah ingin Log Out ?",
                        "Konfirmasi", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE);
                if (opsi != 0){
                    JOptionPane.showMessageDialog(null, "Log Out Dibatalkan!");
                }else {
                    JOptionPane.showMessageDialog(null, "Terima Kasih");
                    dispose();
                    FormLogin show = new FormLogin();
                    show.JPLogin.setVisible(true);
                }
            }
        });
        btnJabatan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                MasterJabatanKaryawan show = new MasterJabatanKaryawan();
                show.JPJabatanKaryawan.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JPJabatanKaryawan);
                lblJudul.setText("MASTER JABATAN KARYAWAN");
            }
        });
    }
}
