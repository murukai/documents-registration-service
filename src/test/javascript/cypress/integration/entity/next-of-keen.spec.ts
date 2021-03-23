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

describe('NextOfKeen e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/next-of-keens*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('next-of-keen');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load NextOfKeens', () => {
    cy.intercept('GET', '/api/next-of-keens*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('next-of-keen');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('NextOfKeen').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details NextOfKeen page', () => {
    cy.intercept('GET', '/api/next-of-keens*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('next-of-keen');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('nextOfKeen');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create NextOfKeen page', () => {
    cy.intercept('GET', '/api/next-of-keens*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('next-of-keen');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('NextOfKeen');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit NextOfKeen page', () => {
    cy.intercept('GET', '/api/next-of-keens*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('next-of-keen');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('NextOfKeen');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of NextOfKeen', () => {
    cy.intercept('GET', '/api/next-of-keens*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('next-of-keen');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('NextOfKeen');

    cy.get(`[data-cy="fullName"]`).type('Implemented', { force: true }).invoke('val').should('match', new RegExp('Implemented'));


    cy.get(`[data-cy="address"]`).type('Quality', { force: true }).invoke('val').should('match', new RegExp('Quality'));


    cy.get(`[data-cy="cellphone"]`).type('Outdoors', { force: true }).invoke('val').should('match', new RegExp('Outdoors'));

    cy.setFieldSelectToLastOfEntity('applicant');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/next-of-keens*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('next-of-keen');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of NextOfKeen', () => {
    cy.intercept('GET', '/api/next-of-keens*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/next-of-keens/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('next-of-keen');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('nextOfKeen').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/next-of-keens*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('next-of-keen');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
