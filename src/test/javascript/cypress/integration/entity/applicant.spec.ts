import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Applicant e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/applicants*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('applicant');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Applicants', () => {
    cy.intercept('GET', '/api/applicants*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('applicant');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Applicant').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Applicant page', () => {
    cy.intercept('GET', '/api/applicants*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('applicant');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('applicant');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Applicant page', () => {
    cy.intercept('GET', '/api/applicants*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('applicant');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Applicant');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Applicant page', () => {
    cy.intercept('GET', '/api/applicants*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('applicant');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Applicant');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of Applicant', () => {
    cy.intercept('GET', '/api/applicants*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('applicant');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('Applicant');

    cy.get(`[data-cy="firstNames"]`).type('hack Administrator', { force: true }).invoke('val').should('match', new RegExp('hack Administrator'));


    cy.get(`[data-cy="lastName"]`).type('Bosco', { force: true }).invoke('val').should('match', new RegExp('Bosco'));


    cy.get(`[data-cy="initials"]`).type('AI Steel', { force: true }).invoke('val').should('match', new RegExp('AI Steel'));


    cy.get(`[data-cy="gender"]`).select('FEMALE');


    cy.get(`[data-cy="maritalStatus"]`).select('SINGLE');


    cy.get(`[data-cy="dateOfBirth"]`).type('2021-03-23').should('have.value', '2021-03-23');


    cy.get(`[data-cy="idNumber"]`).type('bandwidth grey', { force: true }).invoke('val').should('match', new RegExp('bandwidth grey'));


    cy.get(`[data-cy="birthEntryNumber"]`).type('Bedfordshire', { force: true }).invoke('val').should('match', new RegExp('Bedfordshire'));


    cy.get(`[data-cy="eyeColor"]`).select('BLACK');


    cy.get(`[data-cy="hairColor"]`).select('BROWN');


    cy.get(`[data-cy="visibleMarks"]`).type('Towels transform Chad', { force: true }).invoke('val').should('match', new RegExp('Towels transform Chad'));


    cy.get(`[data-cy="profession"]`).type('Assurance Interactions', { force: true }).invoke('val').should('match', new RegExp('Assurance Interactions'));


    cy.setFieldImageAsBytesOfEntity('image', 'integration-test.png', 'image/png');

    cy.setFieldSelectToLastOfEntity('user');

    cy.setFieldSelectToLastOfEntity('appointmentSlot');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/applicants*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('applicant');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of Applicant', () => {
    cy.intercept('GET', '/api/applicants*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/applicants/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('applicant');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('applicant').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/applicants*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('applicant');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
