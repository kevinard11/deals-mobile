package com.okta.examples.service.validation;

import com.okta.examples.model.request.EditProfileRequest;
import com.okta.examples.adapter.status.exception.EditProfileException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserValidation {

    private final String regex_email = "^[\\w!#$%&’+/=?`{|}~^-]+(?:\\.[\\w!#$%&’+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private final String regex_password = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=\\S+$).{8,}$";
    private final String regex_name = "^(?=.{1,9}[a-zA-Z\\'\\-][ ])(?=.*[\\s])(?!.*[0-9])(?!.*[!@#$%^&*]).{3,20}$|^(?=.*[a-zA-Z\\'\\-])(?!.*[0-9])(?!.*[!@#$%^&*]).{3,10}$";

    public void editProfile(EditProfileRequest editProfileRequest, String path){

        boolean content = false;

        if (editProfileRequest.getName() != null) {
            content = true;
            if (!Pattern.matches(regex_name, editProfileRequest.getName())) {
                throw new EditProfileException("Name in invalid.", HttpStatus.BAD_REQUEST);
            }
        }

        if (editProfileRequest.getEmail() != null){
            content = true;
            if (!Pattern.matches(regex_email, editProfileRequest.getEmail())) {
                throw new EditProfileException("Email is invalid.", HttpStatus.BAD_REQUEST);
            }

        }

        if (editProfileRequest.getOldPassword() != null || editProfileRequest.getNewPassword() != null ||
                editProfileRequest.getConfirmPassword() != null) {

            if (editProfileRequest.getOldPassword() != null && editProfileRequest.getNewPassword() != null &&
                    editProfileRequest.getConfirmPassword() != null){

                content = true;
                if (!Pattern.matches(regex_password, editProfileRequest.getOldPassword())){
                    throw new EditProfileException("Password is invalid.", HttpStatus.BAD_REQUEST);
                }

                if (!Pattern.matches(regex_password, editProfileRequest.getNewPassword())){
                    throw new EditProfileException("New Password is invalid.", HttpStatus.BAD_REQUEST);
                }

                if (!editProfileRequest.getConfirmPassword().equals(editProfileRequest.getNewPassword())){
                    throw new EditProfileException("Your new password and confirmation do not match.", HttpStatus.BAD_REQUEST);
                }

            }else {
                throw new EditProfileException("Please fill all password forms!", HttpStatus.BAD_REQUEST);
            }
        }

        if (!content){
            throw new EditProfileException("Please fill at least one field!", HttpStatus.BAD_REQUEST);
        }

    }

}
