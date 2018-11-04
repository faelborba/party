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
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class PartyService {
    private final PartyRepository partyRepository;
    private final ModelMapper modelMapper;

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_PARTY_NOT_FOUND = "Party not found";

    @Autowired
    public PartyService(PartyRepository partyRepository, ModelMapper modelMapper){
        this.partyRepository = partyRepository;
        this.modelMapper = modelMapper;
    }

    public List<PartyOutput> getAll(){
        Type partyOutputListType = new TypeToken<List<PartyOutput>>(){}.getType();
        return modelMapper.map(partyRepository.findAll(), partyOutputListType);
    }

    public PartyOutput create(PartyInput partyInput){
        this.validateInput(partyInput);
        Party party = modelMapper.map(partyInput, Party.class);
        party = partyRepository.save(party);
        return modelMapper.map(party, PartyOutput.class);
    }

    public PartyOutput getById(Long partyId){
        if(partyId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Party party = partyRepository.findById(partyId).orElse(null);
        if(party == null){
            throw new GenericOutputException(MESSAGE_PARTY_NOT_FOUND);
        }

        return modelMapper.map(party, PartyOutput.class);
    }

    public PartyOutput update(Long partyId, PartyInput partyInput){
        if (partyId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }
        validateInput(partyInput);
        Party party = partyRepository.findById(partyId).orElse(null);

        if(party == null){
            throw new GenericOutputException(MESSAGE_PARTY_NOT_FOUND);
        }

        party.setCode(partyInput.getCode());
        party.setName(partyInput.getName());
        party.setNumber(partyInput.getNumber());
        party = partyRepository.save(party);
        return modelMapper.map(party, PartyOutput.class);
    }

    public GenericOutput delete(Long partyId){
        if(partyId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Party party = partyRepository.findById(partyId).orElse(null);
        if(party == null){
            throw new GenericOutputException(MESSAGE_PARTY_NOT_FOUND);
        }

        partyRepository.delete(party);

        return new GenericOutput("Party deleted");
    }

    private void validateInput(PartyInput partyInput){
        if(StringUtils.isBlank(partyInput.getCode())){
            throw new GenericOutputException("Invalid code");
        }
        if(validateCode(partyInput.getCode())){
            throw new GenericOutputException("Invalid code! Code already exists.");
        }
        if(StringUtils.isBlank(partyInput.getName())){
            throw new GenericOutputException("Invalid name");
        }
        if(validateName(partyInput.getName())){
            throw new GenericOutputException("Invalid name");
        }
        if(partyInput.getNumber() == null){
            throw new GenericOutputException("Invalid number");
        }
        if(validateNumber(partyInput.getNumber())){
            throw new GenericOutputException("Invalid number! Number already exists or is in wrong format.");
        }
    }
    private boolean validateName(String name){
        if(name.split(" ")[0].length() < 5){// verificando a quantidade de caracteres do primeiro nome
            return true;
        }
        return false;
    }
    private boolean validateCode(String code){
        List<Party> partyList = (List<Party>) partyRepository.findAll();
        for (Party party : partyList){
            if(party.getCode().equals(code)){
                return true;
            }
        }
        return false;
    }
    private boolean validateNumber(Integer number){
        List<Party> partyList = (List<Party>) partyRepository.findAll();
        if(number.toString().length() != 2){
            return true;
        }
        for (Party party : partyList){
            if(party.getNumber() == number){
                return true;
            }
        }
        return false;
    }
}
