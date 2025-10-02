# Security: [Component/Vulnerability] - YYYY-MM-DD HH:MM

```edn :metadata
{:phase "security"
 :tag #{:security}
 :status :active
 :thinking-mode "ultrathink"
 :date {:created "YYYY-MM-DD"}}
```

## Security Objective

What we're securing:

1. **Asset**: [What needs protection - data, functionality, access]
2. **Threat**: [What we're protecting against]
3. **Impact**: [Consequence if compromised]
4. **Likelihood**: [Probability of exploitation]

### Security Architecture Context (if ADR integration enabled)

Review security decisions: `adr-search tag :security`

**Established security patterns**:
- [Security ADRs that define authentication/authorization models]
- [Cryptographic standards and key management decisions]
- [Security architecture constraints and approved approaches]

## Threat Model

### Attack Vectors

1. **[Attack Vector Name]**
   - Description: [How attack works]
   - Prerequisites: [What attacker needs]
   - Impact: [Damage if successful]
   - Mitigation: [How we prevent/detect]

### Trust Boundaries

- [Boundary 1]: [What crosses, validation required]
- [Boundary 2]: [Access controls needed]

## Security Requirements

- [ ] Authentication: [How users/services prove identity]
- [ ] Authorization: [Permission model]
- [ ] Data Protection: [Encryption at rest/in transit]
- [ ] Audit Logging: [What to log for security]
- [ ] Input Validation: [Sanitization requirements]

## Implementation Log

### [HH:MM] - Security Analysis

**Vulnerability**: [What was found]
**Severity**: [Critical | High | Medium | Low]
**Exploitability**: [How easy to exploit]

### [HH:MM] - Mitigation Applied

**Control**: [Security measure implemented]
**Type**: [Preventive | Detective | Corrective]
**Effectiveness**: [How well it addresses threat]

## Security Testing

### Tests Performed

- [ ] Penetration testing
- [ ] Static code analysis
- [ ] Dependency vulnerability scan
- [ ] Access control verification
- [ ] Encryption verification

### Results

**Vulnerabilities Found**: [Count and severity]
**Mitigations Applied**: [What was fixed]
**Residual Risk**: [Remaining exposure]

## Compliance & Documentation

- [ ] Security review completed
- [ ] Compliance requirements met: [Standards]
- [ ] Security documentation updated
- [ ] Incident response plan updated
- [ ] Security training needs identified

## Verification Checklist

- [ ] Threat model validated
- [ ] All attack vectors addressed
- [ ] Security tests passing
- [ ] Code review by security expert
- [ ] Production security measures verified
- [ ] Monitoring/alerting configured
