package Dashboard;

import DataMaster.FormLogin;
import DataMaster.MasterKaryawan;
import Laporan.LaporanJasaService;
import Laporan.LaporanPenjualan;
import Laporan.LaporanSupplier;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardManajer extends JFrame {
    private JButton btnLaporanSupplier;
    private JButton btnKaryawan;
    private JButton btnLogout;
    public JPanel JPDashboardManajer;
    private JPanel panelform;
    private JLabel lblJudul;
    private JPanel PanelTengah;
    private JPanel PanelForm;
    private JButton btnService;
    private JButton btnPenjualan;

    public void FrameConfig(){
        add(this.JPDashboardManajer);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public DashboardManajer() {
        FrameConfig();
        btnLaporanSupplier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelform.removeAll();
                panelform.revalidate();
                panelform.repaint();
                LaporanSupplier show = new LaporanSupplier();
                show.JPlaporanSupplier.setVisible(true);
                panelform.revalidate();
                panelform.setLayout(new java.awt.BorderLayout());
                panelform.add(show.JPlaporanSupplier);
                lblJudul.setText("LAPORAN SUPPLIER");
            }
        });
        btnLogout.addActionListener(new ActionListener() {
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
        btnService.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelform.removeAll();
                panelform.revalidate();
                panelform.repaint();
                LaporanJasaService show = new LaporanJasaService();
                show.JPLaporanService.setVisible(true);
                panelform.revalidate();
                panelform.setLayout(new java.awt.BorderLayout());
                panelform.add(show.JPLaporanService);
                lblJudul.setText("LAPORAN JASA SERVICE");
            }
        });
        btnPenjualan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelform.removeAll();
                panelform.revalidate();
                panelform.repaint();
                LaporanPenjualan show = new LaporanPenjualan();
                show.JPLaporanPenjualan.setVisible(true);
                panelform.revalidate();
                panelform.setLayout(new java.awt.BorderLayout());
                panelform.add(show.JPLaporanPenjualan);
                lblJudul.setText("LAPORAN PENJUALAN");
            }
        });
    }
}
