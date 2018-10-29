package br.edu.ulbra.election.party.service;

import br.edu.ulbra.election.party.exception.GenericOutputException;
import br.edu.ulbra.election.party.input.v1.PartyInput;
import br.edu.ulbra.election.party.model.Party;
import br.edu.ulbra.election.party.output.v1.GenericOutput;
import br.edu.ulbra.election.party.output.v1.PartyOutput;
import br.edu.ulbra.election.party.repository.PartyRepository;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Type;
import java.security.GeneralSecurityException;
import java.util.List;

public class PartyService {
    private final PartyRepository partyRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_PARTY_NOT_FOUND = "Party not found";

    @Autowired
    public PartyService(PartyRepository partyRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder){
        this.partyRepository = partyRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<PartyOutput> getAll(){
        Type partyOutputListType = new TypeToken<List<PartyOutput>>(){}.getType();
    }

    public PartyOutput create(PartyInput partyInput){
        validateInput(PartyInput,false);
        Party party = modelMapper.map(partyInput, Party.class);
        party = partyRepository.save(party);
        return modelMapper.map(party, PartyOutput.class);
    }

    public PartyOutput getById(Long partyId){
        if(partyId == null){
            throw new GeneralSecurityException(MESSAGE_INVALID_ID);
        }

        Party party = partyRepository.findById(partyId).orElse(null);
        if(party == null){
            throw new GenericOutputException(MESSAGE_PARTY_NOT_FOUND);
        }

        return modelMapper.map(party, PartyOutput.class);
    }

    public PartyOutput Update(Long partyId, PartyInput){

    }

    public GenericOutput delete(Long paryId){

    }

    private void validateInput(PartyInput partyInput, boolean isUpdate){
        if(StringUtils.isBlank(partyInput.getCode())){
            throw new GenericOutputException("Invalid code");
        }
        if(StringUtils.isBlank(partyInput.getName())){
            throw new GenericOutputException("Invalid name");
        }
        if (StringUtils.isBlank(partyInput.getNumber())){
            throws new GenericOutputException("Invalid number");
        }
    }
}
