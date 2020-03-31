/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.helper.ui.memberlist;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author leduy
 */
    public class Member {

        private SimpleStringProperty name;
        private SimpleStringProperty id;
        private SimpleStringProperty email;
        private SimpleStringProperty phone;

        public Member(String name, String id, String phone, String email) {
            this.name = new SimpleStringProperty(name);
            this.id = new SimpleStringProperty(id);
            this.phone = new SimpleStringProperty(phone);
            this.email = new SimpleStringProperty(email);
        }

        public String getName() {
            return name.get();
        }

        public String getId() {
            return id.get();
        }

        public String getPhone() {
            return phone.get();
        }

        public String getEmail() {
            return email.get();
        }
        public boolean containsSomeText(String text){
            //String filteredText = text.toLowerCase();
            if (getName().toLowerCase().contains(text)){
                return true;
            }
            else if (getId().toLowerCase().contains(text)){
                return true;
            }
            else if (getPhone().toLowerCase().contains(text)){
                return true;
            }
            else if (getEmail().toLowerCase().contains(text)){
                return true;
            }
            return false;
        }


    }
