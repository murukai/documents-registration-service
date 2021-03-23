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

describe('MarriageDetails e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/marriage-details*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('marriage-details');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load MarriageDetails', () => {
    cy.intercept('GET', '/api/marriage-details*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('marriage-details');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('MarriageDetails').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details MarriageDetails page', () => {
    cy.intercept('GET', '/api/marriage-details*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('marriage-details');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('marriageDetails');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create MarriageDetails page', () => {
    cy.intercept('GET', '/api/marriage-details*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('marriage-details');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('MarriageDetails');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit MarriageDetails page', () => {
    cy.intercept('GET', '/api/marriage-details*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('marriage-details');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('MarriageDetails');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of MarriageDetails', () => {
    cy.intercept('GET', '/api/marriage-details*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('marriage-details');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('MarriageDetails');

    cy.get(`[data-cy="dateOfMarriage"]`).type('2021-03-23').should('have.value', '2021-03-23');


    cy.get(`[data-cy="spouseFullName"]`).type('Small Agent Granite', { force: true }).invoke('val').should('match', new RegExp('Small Agent Granite'));


    cy.get(`[data-cy="placeOfMarriage"]`).type('Handcrafted Dynamic Gorgeous', { force: true }).invoke('val').should('match', new RegExp('Handcrafted Dynamic Gorgeous'));


    cy.get(`[data-cy="spousePlaceOfBirth"]`).type('up Somoni Berkshire', { force: true }).invoke('val').should('match', new RegExp('up Somoni Berkshire'));


    cy.get(`[data-cy="marriageNumber"]`).type('National navigating', { force: true }).invoke('val').should('match', new RegExp('National navigating'));


    cy.get(`[data-cy="marriedBefore"]`).should('not.be.checked');
    cy.get(`[data-cy="marriedBefore"]`).click().should('be.checked');
    cy.setFieldSelectToLastOfEntity('applicant');

    cy.setFieldSelectToLastOfEntity('countryOfMarriage');

    cy.setFieldSelectToLastOfEntity('spouseCountryOfBirth');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/marriage-details*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('marriage-details');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of MarriageDetails', () => {
    cy.intercept('GET', '/api/marriage-details*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/marriage-details/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('marriage-details');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('marriageDetails').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/marriage-details*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('marriage-details');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
