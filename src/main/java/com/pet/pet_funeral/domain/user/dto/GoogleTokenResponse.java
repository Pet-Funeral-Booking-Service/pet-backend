package com.pet.pet_funeral.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GoogleTokenResponse {

    @JsonProperty("token_type")
    public String tokenType;
    @JsonProperty("access_token")
    public String accessToken;
    @JsonProperty("scope")
    public String scope;
    @JsonProperty("id_token")
    public String idToken;
    @JsonProperty("expires_in")
    public Integer expiresIn;
}
