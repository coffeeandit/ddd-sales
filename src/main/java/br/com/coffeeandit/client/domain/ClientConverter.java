package br.com.coffeeandit.client.domain;

import org.springframework.stereotype.Component;

@Component
public class ClientConverter {

    public ClientEntity fromTransport(final Client client) {
        var clientEntity = new ClientEntity();
        clientEntity.setCpf(client.getCPF());
        clientEntity.setName(client.getNome());
        return clientEntity;
    }

    public Client fromDomain(final ClientEntity client) {
        var clientEntity = new Client(client.getName(), client.getCpf());
        return clientEntity;
    }
}
