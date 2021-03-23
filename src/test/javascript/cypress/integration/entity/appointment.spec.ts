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

describe('Appointment e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/appointments*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('appointment');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Appointments', () => {
    cy.intercept('GET', '/api/appointments*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Appointment').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Appointment page', () => {
    cy.intercept('GET', '/api/appointments*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('appointment');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Appointment page', () => {
    cy.intercept('GET', '/api/appointments*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Appointment');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Appointment page', () => {
    cy.intercept('GET', '/api/appointments*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Appointment');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of Appointment', () => {
    cy.intercept('GET', '/api/appointments*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Appointment');

    cy.get(`[data-cy="dateOfAppointment"]`).type('2021-03-22T19:56').invoke('val').should('equal', '2021-03-22T19:56');

    cy.get(`[data-cy="available"]`).should('not.be.checked');
    cy.get(`[data-cy="available"]`).click().should('be.checked');

    cy.get(`[data-cy="appointmentType"]`).select('ORDINARY');

    cy.get(`[data-cy="maxAppointments"]`).type('967').should('have.value', '967');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/appointments*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of Appointment', () => {
    cy.intercept('GET', '/api/appointments*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/appointments/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('appointment');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('appointment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/appointments*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('appointment');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
