<#import "template.ftl" as layout>
<#import "register-commons.ftl" as registerCommons>
<@layout.registrationLayout displayMessage=!messagesPerField.existsError('firstName','lastName','email','username','password','password-confirm','termsAccepted'); section>
    <#if section = "header">
        ${msg("registerTitle")}
    <#elseif section = "form">
        <link rel="stylesheet"
              href="https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/24.7.0/build/css/intlTelInput.min.css"/>

        <form id="kc-register-form" class="${properties.kcFormClass!}" action="${url.registrationAction}" method="post">
            <div class="${properties.kcFormGroupClass!}">
                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="firstName" class="${properties.kcLabelClass!}">${msg("fullName")}</label>
                </div>
                <div class="${properties.kcInputWrapperClass!}">
                    <input type="text" id="firstName" class="${properties.kcInputClass!}" name="firstName"
                           required
                           value="${(register.formData.firstName!'')}"
                           aria-invalid="<#if messagesPerField.existsError('fullName')>true</#if>"
                    />

                    <#if messagesPerField.existsError('fullName')>
                        <span id="input-error-firstname" class="${properties.kcInputErrorMessageClass!}"
                              aria-live="polite">
                            ${kcSanitize(messagesPerField.get('fullName'))?no_esc}
                        </span>
                    </#if>
                </div>
            </div>

            <div class="${properties.kcFormGroupClass!}">
                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="email" class="${properties.kcLabelClass!}">${msg("email")}</label>
                </div>
                <div class="${properties.kcInputWrapperClass!}">
                    <input type="text" id="email" class="${properties.kcInputClass!}" name="email"
                           required
                           value="${(register.formData.email!'')}" autocomplete="email"
                           aria-invalid="<#if messagesPerField.existsError('email')>true</#if>"
                    />

                    <#if messagesPerField.existsError('email')>
                        <span id="input-error-email" class="${properties.kcInputErrorMessageClass!}" aria-live="polite">
                            ${kcSanitize(messagesPerField.get('email'))?no_esc}
                        </span>
                    </#if>
                </div>
            </div>

            <#if !realm.registrationEmailAsUsername>
                <div class="${properties.kcFormGroupClass!}">
                    <div class="${properties.kcLabelWrapperClass!}">
                        <label for="username" class="${properties.kcLabelClass!}">${msg("username")}</label>
                    </div>
                    <div class="${properties.kcInputWrapperClass!}">
                        <input type="text" id="username" class="${properties.kcInputClass!}" name="username"
                               value="${(register.formData.username!'')}" autocomplete="username"
                               aria-invalid="<#if messagesPerField.existsError('username')>true</#if>"
                        />

                        <#if messagesPerField.existsError('username')>
                            <span id="input-error-username" class="${properties.kcInputErrorMessageClass!}"
                                  aria-live="polite">
                                ${kcSanitize(messagesPerField.get('username'))?no_esc}
                            </span>
                        </#if>
                    </div>
                </div>
            </#if>

            <div class="form-group">
                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="phone" class="${properties.kcLabelClass!}">${msg("phoneNumber")}</label>
                </div>
                <div class="${properties.kcInputWrapperClass!}">
                    <input type="text" id="phone" class="${properties.kcInputClass!}" name="phone"
                           required
                           value="${(register.formData.phone!'')}"
                    />
                </div>
            </div>

            <div class="form-group">
                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="linkedin" class="${properties.kcLabelClass!}">LinkedIn (Optional)</label>
                </div>
                <div class="${properties.kcInputWrapperClass!}">
                    <input type="text" id="linkedin" class="${properties.kcInputClass!}" name="linkedin"
                           value="${(register.formData.linkedin!'')}"
                    />
                </div>
            </div>

            <#if passwordRequired??>
                <div class="${properties.kcFormGroupClass!}">
                    <div class="${properties.kcLabelWrapperClass!}">
                        <label for="password" class="${properties.kcLabelClass!}">${msg("password")}</label>
                    </div>
                    <div class="${properties.kcInputWrapperClass!}">
                        <div class="${properties.kcInputGroup!}">
                            <input type="password" id="password" class="${properties.kcInputClass!}" name="password"
                                   required
                                   autocomplete="new-password"
                                   aria-invalid="<#if messagesPerField.existsError('password','password-confirm')>true</#if>"
                            />
                            <button class="pf-c-button pf-m-control" type="button" aria-label="${msg('showPassword')}"
                                    aria-controls="password" data-password-toggle
                                    data-label-show="${msg('showPassword')}" data-label-hide="${msg('hidePassword')}">
                                <i class="fa fa-eye" aria-hidden="true"></i>
                            </button>
                        </div>


                        <#if messagesPerField.existsError('password')>
                            <span id="input-error-password" class="${properties.kcInputErrorMessageClass!}"
                                  aria-live="polite">
                                ${kcSanitize(messagesPerField.get('password'))?no_esc}
                            </span>
                        </#if>
                    </div>
                </div>

                <div class="${properties.kcFormGroupClass!}">
                    <div class="${properties.kcLabelWrapperClass!}">
                        <label for="password-confirm"
                               class="${properties.kcLabelClass!}">${msg("passwordConfirm")}</label>
                    </div>
                    <div class="${properties.kcInputWrapperClass!}">
                        <div class="${properties.kcInputGroup!}">
                            <input type="password" id="password-confirm" class="${properties.kcInputClass!}"
                                   required
                                   name="password-confirm"
                                   aria-invalid="<#if messagesPerField.existsError('password-confirm')>true</#if>"
                            />
                            <button class="pf-c-button pf-m-control" type="button" aria-label="${msg('showPassword')}"
                                    aria-controls="password-confirm" data-password-toggle
                                    data-label-show="${msg('showPassword')}" data-label-hide="${msg('hidePassword')}">
                                <i class="fa fa-eye" aria-hidden="true"></i>
                            </button>
                        </div>

                        <#if messagesPerField.existsError('password-confirm')>
                            <span id="input-error-password-confirm" class="${properties.kcInputErrorMessageClass!}"
                                  aria-live="polite">
                                ${kcSanitize(messagesPerField.get('password-confirm'))?no_esc}
                            </span>
                        </#if>
                    </div>
                </div>
            </#if>

            <@registerCommons.termsAcceptance/>

            <#if recaptchaRequired??>
                <div class="form-group">
                    <div class="${properties.kcInputWrapperClass!}">
                        <div class="g-recaptcha" data-size="compact" data-sitekey="${recaptchaSiteKey}"></div>
                    </div>
                </div>
            </#if>

            <div class="${properties.kcFormGroupClass!}">
                <div id="kc-form-options" class="${properties.kcFormOptionsClass!}">
                    <div class="${properties.kcFormOptionsWrapperClass!}">
                        <span><a href="${url.loginUrl}">${kcSanitize(msg("backToLogin"))?no_esc}</a></span>
                    </div>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}"
                           type="submit" value="${msg("doRegister")}"/>
                </div>
            </div>
        </form>

        <script type="module" src="${url.resourcesPath}/js/passwordVisibility.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/24.7.0/build/js/intlTelInput.min.js"></script>
        <script>
            const form = document.getElementById("kc-register-form");
            const input = document.getElementById("phone");
            const savedCountry = "${register.formData.country!''}"

            if (form && input) {
                const phoneInput = window.intlTelInput(input, {
                    initialCountry: savedCountry || "us",
                    separateDialCode: true,
                });

                form.addEventListener('submit', function () {

                    const {dialCode, iso2} = phoneInput.getSelectedCountryData();
                    const phoneValue = input.value.trim();
                    const fullPhone = '+' + dialCode + phoneValue;

                    const fullPhoneInput = document.createElement("input");
                    fullPhoneInput.type = "hidden";
                    fullPhoneInput.name = "fullPhone";
                    fullPhoneInput.value = fullPhone;
                    form.appendChild(fullPhoneInput);

                    const countryInput = document.createElement("input");
                    countryInput.type = "hidden";
                    countryInput.name = "country";
                    countryInput.value = iso2;
                    form.appendChild(countryInput);
                });
            }
        </script>

    </#if>
</@layout.registrationLayout>
