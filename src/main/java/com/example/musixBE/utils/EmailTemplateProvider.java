package com.example.musixBE.utils;

public class EmailTemplateProvider {
    public static String buildErrorPage() {
        return "\n" +
                "<!doctype html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>Ooops</title>\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css\" />\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css\" />\n" +
                "\t<style type=\"text/css\">\n" +
                "\n" +
                "    body\n" +
                "    {\n" +
                "        background:#1E1C37;\n" +
                "    }\n" +
                "\n" +
                "    .payment\n" +
                "\t{\n" +
                "\t\tborder:1px solid #f2f2f2;\n" +
                "\t\theight:280px;\n" +
                "        border-radius:20px;\n" +
                "        background:#383676;\n" +
                "\t}\n" +
                "   .payment_header\n" +
                "   {\n" +
                "\t   background:#262345;\n" +
                "\t   padding:20px;\n" +
                "       border-radius:20px 20px 0px 0px;\n" +
                "\t   \n" +
                "   }\n" +
                "   \n" +
                "   .check\n" +
                "   {\n" +
                "\t   margin:0px auto;\n" +
                "\t   width:50px;\n" +
                "\t   height:50px;\n" +
                "\t   border-radius:100%;\n" +
                "\t   background:#FF0000;\n" +
                "\t   text-align:center;\n" +
                "   }\n" +
                "   \n" +
                "   .check i\n" +
                "   {\n" +
                "\t   vertical-align:middle;\n" +
                "\t   line-height:50px;\n" +
                "\t   font-size:30px;\n" +
                "   }\n" +
                "\n" +
                "    .content \n" +
                "    {\n" +
                "        text-align:center;\n" +
                "    }\n" +
                "\n" +
                "    .content  h1\n" +
                "    {\n" +
                "        font-size:25px;\n" +
                "        padding-top:25px;\n" +
                "    }\n" +
                "\n" +
                "    .content a\n" +
                "    {\n" +
                "        width:200px;\n" +
                "        height:35px;\n" +
                "        color:#fff;\n" +
                "        border-radius:30px;\n" +
                "        padding:5px 10px;\n" +
                "        background:#262345;\n" +
                "        transition:all ease-in-out 0.3s;\n" +
                "    }\n" +
                "    .white{\n" +
                "        color:#fff;\n" +
                "    }\n" +
                "    .content a:hover\n" +
                "    {\n" +
                "        text-decoration:none;\n" +
                "        background:#000;\n" +
                "    }\n" +
                "\t\t\n" +
                "\t.video_ad\n" +
                "\t\t{\n" +
                "\t\t\tdisplay: inline-block;\n" +
                "\t\t\twidth: 48%;\n" +
                "\t\t\tmargin-top: 20px;\n" +
                "\t\t\tmargin-left: 1%;\n" +
                "\t\t}\n" +
                "   \n" +
                "\t</style>\n" +
                "\t\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "   <div class=\"container\">\n" +
                "   <div class=\"row\">\n" +
                "      <div class=\"col-md-6 mx-auto mt-5\">\n" +
                "         <div class=\"payment\">\n" +
                "            <div class=\"payment_header\">\n" +
                "               <div class=\"check\"><i class=\"fa fa-close\" aria-hidden=\"true\"></i></div>\n" +
                "            </div>\n" +
                "            <div class=\"content\">\n" +
                "               <h1 class = \"white\">Error</h1>\n" +
                "               <p class = \"white\">The token is either expired or invalid\n" +
                "               </p>\n" +
                "            </div>\n" +
                "            \n" +
                "         </div>\n" +
                "      </div>\n" +
                "   </div>\n" +
                "</div>\n" +
                "   \n" +
                "</body>\n" +
                "</html>\n";
    }

    public static String buildSuccessPage() {
        return "\n" +
                "<!doctype html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>Success</title>\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css\" />\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css\" />\n" +
                "\t<style type=\"text/css\">\n" +
                "\n" +
                "    body\n" +
                "    {\n" +
                "        background:#1E1C37;\n" +
                "    }\n" +
                "\n" +
                "    .payment\n" +
                "\t{\n" +
                "\t\tborder:1px solid #f2f2f2;\n" +
                "\t\theight:280px;\n" +
                "        border-radius:20px;\n" +
                "        background:#383676;\n" +
                "\t}\n" +
                "   .payment_header\n" +
                "   {\n" +
                "\t   background:#262345;\n" +
                "\t   padding:20px;\n" +
                "       border-radius:20px 20px 0px 0px;\n" +
                "\t   \n" +
                "   }\n" +
                "   \n" +
                "   .check\n" +
                "   {\n" +
                "\t   margin:0px auto;\n" +
                "\t   width:50px;\n" +
                "\t   height:50px;\n" +
                "\t   border-radius:100%;\n" +
                "\t   background:#1FDF64;\n" +
                "\t   text-align:center;\n" +
                "   }\n" +
                "   \n" +
                "   .check i\n" +
                "   {\n" +
                "\t   vertical-align:middle;\n" +
                "\t   line-height:50px;\n" +
                "\t   font-size:30px;\n" +
                "   }\n" +
                "\n" +
                "    .content \n" +
                "    {\n" +
                "        text-align:center;\n" +
                "    }\n" +
                "\n" +
                "    .content  h1\n" +
                "    {\n" +
                "        font-size:25px;\n" +
                "        padding-top:25px;\n" +
                "    }\n" +
                "\n" +
                "    .content a\n" +
                "    {\n" +
                "        width:200px;\n" +
                "        height:35px;\n" +
                "        color:#fff;\n" +
                "        border-radius:30px;\n" +
                "        padding:5px 10px;\n" +
                "        background:#262345;\n" +
                "        transition:all ease-in-out 0.3s;\n" +
                "    }\n" +
                "    .white{\n" +
                "        color:#fff;\n" +
                "    }\n" +
                "    .content a:hover\n" +
                "    {\n" +
                "        text-decoration:none;\n" +
                "        background:#000;\n" +
                "    }\n" +
                "\t\t\n" +
                "\t.video_ad\n" +
                "\t\t{\n" +
                "\t\t\tdisplay: inline-block;\n" +
                "\t\t\twidth: 48%;\n" +
                "\t\t\tmargin-top: 20px;\n" +
                "\t\t\tmargin-left: 1%;\n" +
                "\t\t}\n" +
                "   \n" +
                "\t</style>\n" +
                "\t\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "   <div class=\"container\">\n" +
                "   <div class=\"row\">\n" +
                "      <div class=\"col-md-6 mx-auto mt-5\">\n" +
                "         <div class=\"payment\">\n" +
                "            <div class=\"payment_header\">\n" +
                "               <div class=\"check\"><i class=\"fa fa-check\" aria-hidden=\"true\"></i></div>\n" +
                "            </div>\n" +
                "            <div class=\"content\">\n" +
                "               <h1 class = \"white\">Success</h1>\n" +
                "               <p class = \"white\">Your Account has been successfully activated\n" +
                "               </p>\n" +
                "               <p class = \"white\">You may now close this window\n" +
                "               </p>\n" +
                "            </div>\n" +
                "            \n" +
                "         </div>\n" +
                "      </div>\n" +
                "   </div>\n" +
                "</div>\n" +
                "   \n" +
                "</body>\n" +
                "</html>\n";
    }

    public static String buildVerificationEmail(String name, String link) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "    <title></title>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                "    <style type=\"text/css\">\n" +
                "        @media screen {\n" +
                "            @font-face {\n" +
                "                font-family: 'Lato';\n" +
                "                font-style: normal;\n" +
                "                font-weight: 400;\n" +
                "                src: local('Lato Regular'), local('Lato-Regular'), url(https://fonts.gstatic.com/s/lato/v11/qIIYRU-oROkIk8vfvxw6QvesZW2xOQ-xsNqO47m55DA.woff) format('woff');\n" +
                "            }\n" +
                "\n" +
                "            @font-face {\n" +
                "                font-family: 'Lato';\n" +
                "                font-style: normal;\n" +
                "                font-weight: 700;\n" +
                "                src: local('Lato Bold'), local('Lato-Bold'), url(https://fonts.gstatic.com/s/lato/v11/qdgUG4U09HnJwhYI-uK18wLUuEpTyoUstqEm5AMlJo4.woff) format('woff');\n" +
                "            }\n" +
                "\n" +
                "            @font-face {\n" +
                "                font-family: 'Lato';\n" +
                "                font-style: italic;\n" +
                "                font-weight: 400;\n" +
                "                src: local('Lato Italic'), local('Lato-Italic'), url(https://fonts.gstatic.com/s/lato/v11/RYyZNoeFgb0l7W3Vu1aSWOvvDin1pK8aKteLpeZ5c0A.woff) format('woff');\n" +
                "            }\n" +
                "\n" +
                "            @font-face {\n" +
                "                font-family: 'Lato';\n" +
                "                font-style: italic;\n" +
                "                font-weight: 700;\n" +
                "                src: local('Lato Bold Italic'), local('Lato-BoldItalic'), url(https://fonts.gstatic.com/s/lato/v11/HkF_qI1x_noxlxhrhMQYELO3LdcAZYWl9Si6vvxL-qU.woff) format('woff');\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        /* CLIENT-SPECIFIC STYLES */\n" +
                "        body,\n" +
                "        table,\n" +
                "        td,\n" +
                "        a {\n" +
                "            -webkit-text-size-adjust: 100%;\n" +
                "            -ms-text-size-adjust: 100%;\n" +
                "        }\n" +
                "\n" +
                "        table,\n" +
                "        td {\n" +
                "            mso-table-lspace: 0pt;\n" +
                "            mso-table-rspace: 0pt;\n" +
                "        }\n" +
                "\n" +
                "        img {\n" +
                "            -ms-interpolation-mode: bicubic;\n" +
                "        }\n" +
                "\n" +
                "        /* RESET STYLES */\n" +
                "        img {\n" +
                "            border: 0;\n" +
                "            height: auto;\n" +
                "            line-height: 100%;\n" +
                "            outline: none;\n" +
                "            text-decoration: none;\n" +
                "        }\n" +
                "\n" +
                "        table {\n" +
                "            border-collapse: collapse !important;\n" +
                "        }\n" +
                "\n" +
                "        body {\n" +
                "            height: 100% !important;\n" +
                "            margin: 0 !important;\n" +
                "            padding: 0 !important;\n" +
                "            width: 100% !important;\n" +
                "        }\n" +
                "\n" +
                "        /* iOS BLUE LINKS */\n" +
                "        a[x-apple-data-detectors] {\n" +
                "            color: inherit !important;\n" +
                "            text-decoration: none !important;\n" +
                "            font-size: inherit !important;\n" +
                "            font-family: inherit !important;\n" +
                "            font-weight: inherit !important;\n" +
                "            line-height: inherit !important;\n" +
                "        }\n" +
                "\n" +
                "        /* MOBILE STYLES */\n" +
                "        @media screen and (max-width:600px) {\n" +
                "            h1 {\n" +
                "                font-size: 32px !important;\n" +
                "                line-height: 32px !important;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        /* ANDROID CENTER FIX */\n" +
                "        div[style*=\"margin: 16px 0;\"] {\n" +
                "            margin: 0 !important;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body style=\"background-color: #1FDF64; margin: 0 !important; padding: 0 !important;\">\n" +
                "    <!-- HIDDEN PREHEADER TEXT -->\n" +
                "    <div style=\"display: none; font-size: 1px; color: #fefefe; line-height: 1px; font-family: 'Lato', Helvetica, Arial, sans-serif; max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden;\"> We're thrilled to have you here! Get ready to dive into your new account.\n" +
                "    </div>\n" +
                "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "        <!-- LOGO -->\n" +
                "        <tr>\n" +
                "            <td bgcolor=\"#1FDF64\" align=\"center\">\n" +
                "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                "                    <tr>\n" +
                "                        <td align=\"center\" valign=\"top\" style=\"padding: 40px 10px 40px 10px;\"> </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td bgcolor=\"#1FDF64\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\">\n" +
                "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#1E1C37\" align=\"center\" valign=\"top\" style=\"padding: 40px 20px 20px 20px; border-radius: 4px 4px 0px 0px; color: #ffff; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 48px; font-weight: 400; letter-spacing: 4px; line-height: 48px;\">\n" +
                "                            <h1 style=\"font-size: 48px; font-weight: 400; margin: 2;\">Welcome! " + name + "</h1> <img src=\"https://i.ibb.co/7nDJhT1/musiX.png\" width=\"100\" height=\"120\" style=\"display: block; border: 0px;\" />\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td bgcolor=\"#1FDF64\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\">\n" +
                "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#1E1C37\" align=\"left\" style=\"padding: 20px 30px 40px 30px; color: #ffff; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\n" +
                "                            <p style=\"margin: 0;\">We're excited to have you get started. First, you need to confirm your account. Just press the button below.</p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#ffffff\" align=\"left\">\n" +
                "                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                <tr>\n" +
                "                                    <td bgcolor=\"#1E1C37\" align=\"center\" style=\"padding: 20px 30px 60px 30px;\">\n" +
                "                                        <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                            <tr>\n" +
                "                                                <td align=\"center\" style=\"border-radius: 3px;\" bgcolor=\"#69E897\"><a href=\"" + link + "\" target=\"_blank\" style=\"font-size: 20px; font-family: Helvetica, Arial, sans-serif; color: #ffffff; text-decoration: none; color: #ffffff; text-decoration: none; padding: 15px 25px; border-radius: 2px; border: 1px solid #69E897; display: inline-block;\">Confirm Account</a></td>\n" +
                "                                            </tr>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                        </td>\n" +
                "                    </tr> <!-- COPY -->\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#1E1C37\" align=\"left\" style=\"padding: 0px 30px 0px 30px; color: #ffff; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\n" +
                "                            <p style=\"margin: 0;\">If that doesn't work, copy and paste the following link in your browser:</p>\n" +
                "                        </td>\n" +
                "                    </tr> <!-- COPY -->\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#1E1C37\" align=\"left\" style=\"padding: 20px 30px 20px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\n" +
                "                            <p style=\"margin: 0;\"><a href=\"" + link + "\" target=\"_blank\" style=\"color: #1FDF64;\">" + link + "</a></p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#1E1C37\" align=\"left\" style=\"padding: 0px 30px 20px 30px; color: #ffff; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\n" +
                "                            <p style=\"margin: 0;\">If you have any questions, just reply to this email&mdash;we're always happy to help out.</p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#1E1C37\" align=\"left\" style=\"padding: 0px 30px 40px 30px; border-radius: 0px 0px 4px 4px; color: #ffff; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\n" +
                "                            <p style=\"margin: 0;\">Cheers,<br>Musix Team</p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        \n" +
                "    </table>\n" +
                "</body>\n" +
                "\n" +
                "</html>";
    }
}
