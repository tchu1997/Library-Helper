/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.helper.settingsButton;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import library.helper.fxdialogs.FxAlerts;
import org.apache.commons.codec.digest.DigestUtils;

public class SettingWrapper {

    public static final String CONFIGURATION_FILE = "configuration.txt";

    int borrowPeriod;
    double finePerDay;
    String username;
    String password;

    public SettingWrapper() {
        borrowPeriod = 7;
        finePerDay = 1;
        username = "admin";
        setPassword("admin");
    }

    public int getBorrowPeriod() {
        return borrowPeriod;
    }

    public void setBorrowPeriod(int borrowPeriod) {
        this.borrowPeriod = borrowPeriod;
    }

    public double getFinePerDay() {
        return finePerDay;
    }

    public void setFinePerDay(double finePerDay) {
        this.finePerDay = finePerDay;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = (password.length() < 16) ? DigestUtils.shaHex(password) : password;

    }

    public static void initConfiguration() {

        Writer writer = null;
        try {
            SettingWrapper settingWrapper = new SettingWrapper();
            Gson gson = new Gson();
            writer = new FileWriter(CONFIGURATION_FILE);
            gson.toJson(settingWrapper, writer);
        } catch (IOException ex) {
            Logger.getLogger(SettingWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(SettingWrapper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static SettingWrapper getSettingWrapper() {
        Gson gson = new Gson();
        SettingWrapper settingWrapper = new SettingWrapper();
        try {
            settingWrapper = gson.fromJson(new FileReader(CONFIGURATION_FILE), SettingWrapper.class);
        } catch (FileNotFoundException ex) {
            initConfiguration();
            Logger.getLogger(SettingWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return settingWrapper;
    }

    public static void writeSettingtoFile(SettingWrapper settingWrapper) {
        Writer writer = null;
        try {
            Gson gson = new Gson();
            writer = new FileWriter(CONFIGURATION_FILE);
            gson.toJson(settingWrapper, writer);
            FxAlerts.showInformation("Success!", "Settings has been updated.");
        } catch (IOException ex) {
            Logger.getLogger(SettingWrapper.class.getName()).log(Level.SEVERE, null, ex);
            FxAlerts.showException("Failed", "Cannot find config file.", ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(SettingWrapper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
