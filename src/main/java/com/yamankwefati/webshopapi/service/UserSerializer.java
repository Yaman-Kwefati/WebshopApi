package com.yamankwefati.webshopapi.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.yamankwefati.webshopapi.model.User;

import java.io.IOException;

public class UserSerializer extends JsonSerializer<User> {
    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", user.getId());
        jsonGenerator.writeStringField("firstname", user.getFirstname());
        jsonGenerator.writeStringField("lastname", user.getLastname());
        jsonGenerator.writeStringField("email", user.getEmail());
        jsonGenerator.writeStringField("phoneNumber", user.getPhoneNumber());
        jsonGenerator.writeStringField("city", user.getCity());
        jsonGenerator.writeStringField("street", user.getStreet());
        jsonGenerator.writeStringField("postalCode", user.getPostalCode());
        jsonGenerator.writeStringField("userRol", user.getUserRol().name());
        jsonGenerator.writeEndObject();
    }
}
